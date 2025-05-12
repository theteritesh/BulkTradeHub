package com.technoworld.BulkTradeHub.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.technoworld.BulkTradeHub.entity.Cart;
import com.technoworld.BulkTradeHub.entity.Product;
import com.technoworld.BulkTradeHub.entity.ProductPost;
import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.repository.CartRepository;
import com.technoworld.BulkTradeHub.repository.ProductPostRepository;
import com.technoworld.BulkTradeHub.repository.ProductRepository;
import com.technoworld.BulkTradeHub.service.RazorpayService;

@Controller
@RequestMapping("/allPermit")
public class AllPermiteController {
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ProductPostRepository productPostRepository; 
	
	@Autowired
	private ProductRepository productRepository;
	
	 @Autowired
	  private RazorpayService razorpayService;


	
	@GetMapping("/addToCart")
	public String addToCart(Model model,Principal principal) {
		User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
		List<Cart> cartsList=cartRepository.findByUserAndStatusOrderByAddedAtDesc(user,true);
		
		Map<Long, String> productImageMap = new HashMap<>();
		double subTotal=0;
		double discountTotal=0;
		double finalPayble=0; 
		int totalProducts=0;
	    for (Cart cart : cartsList) {
	        Product product = cart.getProduct();
	        if (product != null && product.getMainImage() != null) {
	            String base64Image = Base64.getEncoder().encodeToString(product.getMainImage());
	            productImageMap.put(product.getId(), base64Image);
	        }
	        subTotal=subTotal+((cart.getProductPost().getRetailPrice())*(cart.getProductPost().getMinOrderQuantity())*(cart.getQuantity()));
	        finalPayble=finalPayble+((cart.getProductPost().getWholesalePrice())*(cart.getProductPost().getMinOrderQuantity())*(cart.getQuantity()));
	        totalProducts=totalProducts+cart.getQuantity();
	    }
	    
		
		model.addAttribute("cartList",cartsList);
		model.addAttribute("productImageMap", productImageMap);
		model.addAttribute("subTotal", subTotal);
		model.addAttribute("finalPayble", finalPayble);
		model.addAttribute("totalProducts", totalProducts);
		model.addAttribute("discountTotal", (subTotal-finalPayble));
		return "addToCart";
	}
	
	@GetMapping("/addProductToCart")
	public ResponseEntity<?> addProductToCart(
			@RequestParam("productPostId") int productPostId,
			@RequestParam("quntity") int quntity,
			Principal principal) {
		User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
	    Optional<ProductPost> productPostOpt = productPostRepository.findById(productPostId);
	    
	    HashMap<String, Object> responce=new HashMap<>();

	    if (user == null || productPostOpt.isEmpty()) {
	        return ResponseEntity.badRequest().body("Invalid product or user.");
	    }

	    ProductPost productPost = productPostOpt.get();
	    long productId = productPost.getProductId();
	    Product product=productRepository.findById(productId).get();

	    Optional<Cart> existingCart = cartRepository.findByUserAndProductPost(user, productPost);

	    Cart cart;
	    if (existingCart.isPresent()) {
	        cart = existingCart.get();
	        cart.setQuantity(cart.getQuantity() + quntity);
	        cart.setStatus(true);
	        cart.setAddedAt(LocalDateTime.now());
	    } else {
	        cart = new Cart();
	        cart.setUser(user);
	        cart.setProductPost(productPost);
	        cart.setProduct(product);
	        cart.setQuantity(quntity);
	        cart.setStatus(true);
	        cart.setAddedAt(LocalDateTime.now());
	    }

	   Cart savedCart=cartRepository.save(cart);
	   responce.put("cartId", savedCart.getId());
	   responce.put("userId", savedCart.getUser().getId());
	   responce.put("productPostId", savedCart.getProductPost().getId());
	   responce.put("productId", savedCart.getProduct().getId());
	   responce.put("quantity", savedCart.getQuantity());
	    return ResponseEntity.ok(responce);
	}
	
	@PostMapping("/updateCartQuantity")
	public ResponseEntity<?> updateCartQuantity(
	        @RequestBody Map<String, Object> requestData,
	        Principal principal) {

	    // Extract the values from the request body map
	    Integer cartId = (Integer) requestData.get("cartId");
	    String action = (String) requestData.get("action");

	    User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
	    Optional<Cart> cartOpt = cartRepository.findById(cartId);

	    if (cartOpt.isEmpty()) {
	        return ResponseEntity.badRequest().body("Cart item not found.");
	    }

	    Cart cart = cartOpt.get();

	    // Ensure cart belongs to the logged-in user
	    if (cart.getUser().getId() != user.getId()) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access.");
	    }

	    int quantity = cart.getQuantity();
	    if ("increase".equalsIgnoreCase(action)) {
	        cart.setQuantity(quantity + 1);
	    } else if ("decrease".equalsIgnoreCase(action) && quantity > 1) {
	        cart.setQuantity(quantity - 1);
	    } else {
	        return ResponseEntity.badRequest().body("Invalid action or minimum quantity reached.");
	    }
	    
	    cartRepository.save(cart);

	    Map<String, Object> response = new HashMap<>();
	    response.put("cartId", cart.getId());
	    response.put("newQuantity", cart.getQuantity());
	    response.put("totalWholesalePrice", cart.getProductPost().getWholesalePrice() * cart.getQuantity());
	    response.put("totalRetailPrice", cart.getProductPost().getRetailPrice() * cart.getQuantity());

	    return ResponseEntity.ok(response);
	}
	
	@DeleteMapping("/deleteCartItem/{cartId}")
	public ResponseEntity<?> deleteCartItem(@PathVariable Integer cartId, Principal principal) {

	    User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
	    Optional<Cart> cartOpt = cartRepository.findById(cartId);

	    if (cartOpt.isEmpty()) {
	        return ResponseEntity.badRequest().body("Cart item not found.");
	    }

	    Cart cart = cartOpt.get();

	    if (cart.getUser().getId() != user.getId()) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access.");
	    }

	    cartRepository.delete(cart);
	    return ResponseEntity.ok("Cart item deleted successfully.");
	}
	
	@PostMapping("/create-order")
	public ResponseEntity<Map<String, Object>> createOrder(@RequestParam Double amount,Principal principal) throws Exception {
	    Map<String, Object> order = razorpayService.createOrderObject(amount,principal);
	    return ResponseEntity.ok(order);
	}

	@GetMapping("/clearCart")
	public ResponseEntity<?> clearCart(Principal principal){
		User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
		List<Cart> cartsList=cartRepository.findByUserAndStatusOrderByAddedAtDesc(user,true);
		for (Cart cart : cartsList) {
			cart.setQuantity(0);
			cart.setStatus(false);
			cartRepository.save(cart);
	    }
		return ResponseEntity.ok("Cart Cleared");
	}

}
