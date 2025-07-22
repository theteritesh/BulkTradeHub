package com.technoworld.BulkTradeHub.controller;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.technoworld.BulkTradeHub.entity.Cart;
import com.technoworld.BulkTradeHub.entity.Contact;
import com.technoworld.BulkTradeHub.entity.OrderItems;
import com.technoworld.BulkTradeHub.entity.Product;
import com.technoworld.BulkTradeHub.entity.ProductPost;
import com.technoworld.BulkTradeHub.entity.RazorpayCredentials;
import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.entity.UserOrders;
import com.technoworld.BulkTradeHub.repository.CartRepository;
import com.technoworld.BulkTradeHub.repository.OrderItemsRepository;
import com.technoworld.BulkTradeHub.repository.ProductPostRepository;
import com.technoworld.BulkTradeHub.repository.ProductRepository;
import com.technoworld.BulkTradeHub.repository.RazorpayCredentialsRepository;
import com.technoworld.BulkTradeHub.repository.UserOrderRepository;
import com.technoworld.BulkTradeHub.repository.UserRepository;
import com.technoworld.BulkTradeHub.service.ContactService;


@Controller
@RequestMapping("/home")
public class HomeController {
	
	@Autowired
	private ProductPostRepository productPostRepository; 
	
	@Autowired
	private ProductRepository productRepository;

	@Autowired 
	private ContactService contactService;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private RazorpayCredentialsRepository razorpayCredentialsRepository;
	
	@Autowired
	private OrderItemsRepository orderItemsRepository;
	
	@Autowired
	private UserOrderRepository userOrderRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("")
    public String displayHome() {
		 return "index";
	}

	
	@PostMapping("/contact")
	public String saveContactMessage(Contact contact) {
		contactService.saveContactMessage(contact); 
		return "redirect:/home";
	}
	
	@GetMapping("/productDisplay/{productPostId}")
	public String getProductDetails(@PathVariable("productPostId") Integer productPostId,
			@RequestParam(defaultValue = "0") int page,
			Model model) {
	    Optional<ProductPost> productPostOptional = productPostRepository.findById(productPostId);
	    ProductPost productPost = productPostOptional.get();
	    

	    Optional<Product> productOptional = productRepository.findById(productPost.getProductId());
	    Product product = productOptional.get();

	    // Main Image
	    String mainImageBase64 = null;
	    if (product.getMainImage() != null) {
	        mainImageBase64 = Base64.getEncoder().encodeToString(product.getMainImage());
	    }

	    // First Image
	    String firstImageBase64 = null;
	    if (product.getFirstImage() != null) {
	        firstImageBase64 = Base64.getEncoder().encodeToString(product.getFirstImage());
	    }

	    // Second Image
	    String secondImageBase64 = null;
	    if (product.getSecondImage() != null) {
	        secondImageBase64 = Base64.getEncoder().encodeToString(product.getSecondImage());
	    }

	    // Third Image
	    String thirdImageBase64 = null;
	    if (product.getThirdImage() != null) {
	        thirdImageBase64 = Base64.getEncoder().encodeToString(product.getThirdImage());
	    }

	    // Fourth Image
	    String fourthImageBase64 = null;
	    if (product.getFourthImage() != null) {
	        fourthImageBase64 = Base64.getEncoder().encodeToString(product.getFourthImage());
	    }

	    // Fifth Image
	    String fifthImageBase64 = null;
	    if (product.getFifthImage() != null) {
	        fifthImageBase64 = Base64.getEncoder().encodeToString(product.getFifthImage());
	    }
	    
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
        String formattedPostedAt = productPost.getPostedAt().format(formatter);
        
        model.addAttribute("formattedPostedAt", formattedPostedAt);
	    model.addAttribute("productPost", productPost);
	    model.addAttribute("product", product);
	    model.addAttribute("mainImageBase64", mainImageBase64);
	    model.addAttribute("firstImageBase64", firstImageBase64);
	    model.addAttribute("secondImageBase64", secondImageBase64);
	    model.addAttribute("thirdImageBase64", thirdImageBase64);
	    model.addAttribute("fourthImageBase64", fourthImageBase64);
	    model.addAttribute("fifthImageBase64", fifthImageBase64);

	    return "/productDetails";
	}

	@GetMapping("/productPost")
	@ResponseBody
	public Map<String, Object> getProductPost(@RequestParam(defaultValue = "0") int page) {
	    int pageSize = 16;
	    Page<ProductPost> productPostPage = productPostRepository.findAllByOrderByPostedAtDesc(PageRequest.of(page, pageSize));

	    List<Map<String, Object>> products = productPostPage.getContent().stream().map(post -> {
	        Map<String, Object> productMap = new HashMap<>();
	        productMap.put("id", post.getId());
	        productMap.put("productName", post.getProductName());
	        productMap.put("productId", post.getProductId());
	        productMap.put("minOrderQuantity", post.getMinOrderQuantity());
	        productMap.put("wholesalePrice", post.getWholesalePrice());
	        productMap.put("userId", post.getUser() != null ? post.getUser().getId() : null);

	        // ✅ Fetch image and encode to base64
	        Optional<Product> productOpt = productRepository.findById(post.getProductId());
	        if (productOpt.isPresent() && productOpt.get().getMainImage() != null) {
	            byte[] imageBytes = productOpt.get().getMainImage();
	            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
	            String dataUrl = "data:image/jpeg;base64," + base64Image;
	            productMap.put("imageData", dataUrl);
	        } else {
	            productMap.put("imageData", null); 
	        }

	        return productMap;
	    }).collect(Collectors.toList());

	    Map<String, Object> response = new HashMap<>();
	    response.put("products", products);
	    response.put("currentPage", page);
	    response.put("totalPages", productPostPage.getTotalPages());

	    return response;
	}
	
	@GetMapping("/productPost/related-and-new")
	@ResponseBody
	public Map<String, List<Map<String, Object>>> getRelatedAndNewProducts(@RequestParam String category) {
	    int limit = 4;
	    PageRequest pageRequest = PageRequest.of(0, limit);

	    List<ProductPost> related = productPostRepository.findByCategoryOrderByPostedAtDesc(category, pageRequest).getContent();
	    List<ProductPost> newProducts = productPostRepository.findAllByOrderByPostedAtDesc(pageRequest).getContent();

	    Function<ProductPost, Map<String, Object>> toMap = post -> {
	        Map<String, Object> map = new HashMap<>();
	        map.put("id", post.getId());
	        map.put("productName", post.getProductName());
	        map.put("minOrderQuantity", post.getMinOrderQuantity());
	        map.put("wholesalePrice", post.getWholesalePrice());

	        productRepository.findById(post.getProductId()).ifPresent(product -> {
	            byte[] imageBytes = product.getMainImage();
	            if (imageBytes != null) {
	                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
	                map.put("base64Image", "data:image/jpeg;base64," + base64Image);
	            }
	        });

	        return map;
	    };

	    Map<String, List<Map<String, Object>>> response = new HashMap<>();
	    response.put("related", related.stream().map(toMap).toList());
	    response.put("new", newProducts.stream().map(toMap).toList());

	    return response;
	}
	
	@GetMapping("/getCart")
	public String getCart() {
		return "cart";
	}
	
	@PostMapping("/mergeGuestCart")
	public ResponseEntity<?> mergeGuestCart(@RequestBody List<Map<String, Object>> guestItems,Principal principal){
		User user =  (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
		
		
		for (Map<String, Object> item : guestItems) {
	        Integer productPostId = (Integer) item.get("productPostId");
	        Integer quantity = (Integer) item.get("quantity");
	        String addedAt = (String) item.get("addedAt");
	        
	        LocalDateTime addedDateTime = LocalDateTime.parse(addedAt, DateTimeFormatter.ISO_DATE_TIME);

	        ProductPost productPost = productPostRepository.findById(productPostId)
	                .orElseThrow(() -> new RuntimeException("ProductPost not found: " + productPostId));

	        // Save into Cart table
	        Cart cartItem = new Cart();
	        cartItem.setUserId(user.getId());
	        cartItem.setProductPostId(productPostId);
	        cartItem.setProductId(productPost.getProductId());
	        cartItem.setQuantity(quantity);
	        cartItem.setAddedAt(addedDateTime);

	        Cart existing = cartRepository.findByUserIdAndProductPostId(user.getId(), productPostId);
	        if (existing != null) {
	            existing.setQuantity(existing.getQuantity() + quantity);
	            existing.setAddedAt(addedDateTime);
	            cartRepository.save(existing);
	        } else {
	            cartRepository.save(cartItem);
	        }

	        
	    }
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/getLoggedInUserCart")
	@ResponseBody
	public ResponseEntity<?> getLoggedInUserCart(Principal principal) {
	    try {
	        // Ensure user is authenticated
	        if (principal == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                    .body("User not authenticated");
	        }

	        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

	        // Fetch cart items from native query
	        List<Object[]> results = cartRepository.findCartItemsWithDetails(user.getId());

	        // Transform Object[] results to a List of Maps (JSON-compatible)
	        List<Map<String, Object>> cartItems = new ArrayList<>();

	        for (Object[] row : results) {
	            Map<String, Object> item = new HashMap<>();
	            item.put("productPostId", row[0]);
	            item.put("quantity", row[1]);
	            item.put("addedAt", row[2]);
	            item.put("productName", row[3]);
	            item.put("wholesalePrice", row[4]);
	            item.put("retailPrice", row[5]);
	            item.put("minOrderQuantity", row[6]);
	            item.put("availableLots", row[7]);
	            item.put("category", row[8]);
	            item.put("brand", row[9]);
	            String base64Image = row[10] != null
	            	    ? "data:image/jpeg;base64," + Base64.getEncoder().encodeToString((byte[]) row[10])
	            	    : null;
	            item.put("mainImageBase64", base64Image);
	            item.put("cartId", row[11]);
	            item.put("sellerName", row[12]);
	            cartItems.add(item);
	        }

	        return ResponseEntity.ok(cartItems);

	    } catch (Exception ex) {
	        ex.printStackTrace(); // Optional: Log it with logger
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An error occurred while fetching cart items.");
	    }
	}
	
	@PostMapping("/cart/delete/{id}")
	@ResponseBody
	public ResponseEntity<?> deleteCartItem(@PathVariable("id") int id, Principal principal) {
	    try {
	        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
	        Optional<Cart> optionalCart = cartRepository.findById(id);

	        if (optionalCart.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart item not found");
	        }

	        Cart cart = optionalCart.get();

	        if (cart.getUserId() != user.getId()) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized to delete this cart item");
	        }

	        cartRepository.deleteById(id);
	        return ResponseEntity.ok(cart); // 🔁 Send deleted cart back
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete cart item");
	    }
	}

	@PostMapping("/cart/increase/{id}")
	@ResponseBody
	public ResponseEntity<?> increaseCartItem(@PathVariable("id") int id, Principal principal) {
	    try {
	        if (principal == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
	        }

	        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

	        Optional<Cart> optionalCart = cartRepository.findById(id);
	        if (optionalCart.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart item not found");
	        }

	        Cart cart = optionalCart.get();

	        // Check ownership
	        if (cart.getUserId() != user.getId()) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access to cart item");
	        }

	        ProductPost productPost = productPostRepository.findById(cart.getProductPostId()).get();
	        int maxLots = productPost.getLots();
	        if (cart.getQuantity() >= maxLots) {
	            return ResponseEntity.badRequest().body("No more lots available to increase");
	        }

	        // Increase quantity
	        cart.setQuantity(cart.getQuantity() + 1);
	        Cart updatedCart = cartRepository.save(cart);

	        return ResponseEntity.ok(updatedCart);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Failed to increase cart item quantity");
	    }
	}
	
	@PostMapping("/cart/decrease/{id}")
	@ResponseBody
	public ResponseEntity<?> decreaseCartItem(@PathVariable("id") int id, Principal principal) {
	    try {
	        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
	        Optional<Cart> optionalCart = cartRepository.findById(id);

	        if (optionalCart.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart item not found");
	        }

	        Cart cart = optionalCart.get();

	        if (cart.getUserId() != user.getId()) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized to modify this cart item");
	        }

	        if (cart.getQuantity() <= 1) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Minimum quantity reached");
	        }

	        cart.setQuantity(cart.getQuantity() - 1);
	        Cart updatedCart = cartRepository.save(cart);
	        
	        return ResponseEntity.ok(updatedCart);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to decrease cart item");
	    }
	}

	@PostMapping("/cart/clear")
	@ResponseBody
	public ResponseEntity<?> clearCart(Principal principal) {
	    try {
	        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
	        int userId = user.getId();
	        cartRepository.deleteAllByUserId(userId);

	        return ResponseEntity.ok("Cart cleared");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Failed to clear cart");
	    }
	}

	@PostMapping("/cart/add")
	public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> item, Principal principal) {
	    User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

	    Integer productPostId = (Integer) item.get("productPostId");
	    Integer quantity = (Integer) item.get("quantity");
	    String addedAt = (String) item.get("addedAt");

	    LocalDateTime addedDateTime = LocalDateTime.parse(addedAt, DateTimeFormatter.ISO_DATE_TIME);

	    ProductPost productPost = productPostRepository.findById(productPostId)
	            .orElseThrow(() -> new RuntimeException("ProductPost not found: " + productPostId));

	    Cart existing = cartRepository.findByUserIdAndProductPostId(user.getId(), productPostId);
	    if (existing != null) {
	        existing.setQuantity(existing.getQuantity() + quantity);
	        existing.setAddedAt(addedDateTime);
	        cartRepository.save(existing);
	    } else {
	        Cart cartItem = new Cart();
	        cartItem.setUserId(user.getId());
	        cartItem.setProductPostId(productPostId);
	        cartItem.setProductId(productPost.getProductId());
	        cartItem.setQuantity(quantity);
	        cartItem.setAddedAt(addedDateTime);
	        cartRepository.save(cartItem);
	    }

	    return ResponseEntity.ok().build();
	}

	@GetMapping("/cart/count")
	@ResponseBody
	public ResponseEntity<Map<String, Integer>> getCartItemCount(Principal principal) {
		User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("count", 0));
	    }

	    int count = cartRepository.countByUserId(user.getId());
	    return ResponseEntity.ok(Map.of("count", count));
	}
	
	@PostMapping("/create-razorpay-order")
	public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> request, Principal principal) throws RazorpayException {
		User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
		Optional<RazorpayCredentials> razorPayOptional = razorpayCredentialsRepository.findById(1);

		if (razorPayOptional.isEmpty()) {
			return ResponseEntity.ok("Please contact admin!");
		}

		RazorpayCredentials razorpayCredentials = razorPayOptional.get();
		RazorpayClient razorpay = new RazorpayClient(razorpayCredentials.getKeyId(), razorpayCredentials.getKeySecret());

		double amount = Double.parseDouble(request.get("amount").toString());

		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount", (int) (amount * 100)); // paise
		orderRequest.put("currency", "INR");
		orderRequest.put("receipt", UUID.randomUUID().toString());

		Order order = razorpay.orders.create(orderRequest);

		Map<String, Object> response = new HashMap<>();
		response.put("orderId", order.get("id"));
		response.put("amount", order.get("amount"));
		response.put("currency", order.get("currency"));
		response.put("status", order.get("status"));
		response.put("receipt", order.get("receipt"));
		response.put("created_at", order.get("created_at"));
		response.put("userName", user.getName());
		response.put("userEmail", user.getEmail());
		response.put("userPhone", user.getProfile().getPhoneNumber());
		response.put("keyId", razorpayCredentials.getKeyId());

		return ResponseEntity.ok(response);
	}

	@PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> payload, Principal principal) {
		User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
		Optional<RazorpayCredentials> razorPayOptional = razorpayCredentialsRepository.findById(1);
		RazorpayCredentials razorpayCredentials = razorPayOptional.get();
		HashMap<String, Object> responce= new HashMap<>();
		
		if (razorPayOptional.isEmpty()) {
			responce.put("msg", "Please contact admin!");
			return ResponseEntity.ok(responce);
		}
		
        String razorpayPaymentId = payload.get("razorpay_payment_id");
        String razorpayOrderId = payload.get("razorpay_order_id");
        String razorpaySignature = payload.get("razorpay_signature");
        double amount= Double.parseDouble(payload.get("amount"))/100;

        String secret =razorpayCredentials.getKeySecret();

        boolean isValid = isValidSignature(
                razorpayOrderId,
                razorpayPaymentId,
                razorpaySignature,
                secret
        );

        if (isValid) {
        	List<Cart> byUserIdOrderByAddedAtDesc = cartRepository.findByUserIdOrderByAddedAtDesc(user.getId());
        	
        	UserOrders order = new UserOrders();
        	order.setBuyerId(user.getId());
        	order.setRazorpayOrderId(razorpayOrderId);
        	order.setRazorpayPaymentId(razorpayPaymentId);
        	order.setFinalAmount(amount);
        	order.setStatus("PAID");
        	order.setCreatedAt(LocalDateTime.now());
        	
        	UserOrders userOrder = userOrderRepository.save(order);
        	
        	for(Cart item : byUserIdOrderByAddedAtDesc) {
        		Optional<ProductPost> productPostOptional = productPostRepository.findById(item.getProductPostId());
        		ProductPost productPost = productPostOptional.get();
        		Double lotProce = productPost.getWholesalePrice()*productPost.getMinOrderQuantity();
        		
        		productPost.setLots(productPost.getLots() - item.getQuantity());
        		
        		OrderItems orderItem = new OrderItems();
        		orderItem.setBuyerId(item.getUserId());
        		orderItem.setLotsQuntity(item.getQuantity());
        		orderItem.setProductPostId(item.getProductPostId());
        		orderItem.setSellerId(productPost.getUser().getId());
        		orderItem.setLotPrice(lotProce);
        		orderItem.setSubTotal(lotProce * item.getQuantity());
        		orderItem.setOrderId(userOrder.getId());
        		
        		orderItemsRepository.save(orderItem);
        		productPostRepository.save(productPost);
        		cartRepository.delete(item);
        	}
        	
        	responce.put("orderId", userOrder.getId());
        	
            return ResponseEntity.ok(responce);
        } else {
            return ResponseEntity.badRequest().body("❌ Payment verification failed");
        }
    }
	
	boolean isValidSignature(String orderId, String paymentId, String razorpaySignature, String secret) {
        try {
            String payload = orderId + "|" + paymentId;

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secretKey);

            byte[] hash = sha256_HMAC.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String generatedSignature = new String(Hex.encode(hash));

            return generatedSignature.equals(razorpaySignature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
	
	@GetMapping("/secure/orderSucess/{orderId}") 
	public String getOrderSucess(@PathVariable("orderId") int id,Model model) {
		model.addAttribute("oderId",id);
		return "/orderSucess";
	}
	
	@GetMapping("/secure/getOrderDetails/{orderId}")
	@ResponseBody
	public ResponseEntity<?> getOrderDetails(@PathVariable("orderId") int id, Principal principal) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	        // Get logged-in user
	        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

	        // Fetch the order for that user
	        Optional<UserOrders> orderOptional = userOrderRepository.findByIdAndBuyerId(id, user.getId());
	        if (orderOptional.isEmpty()) {
	            response.put("error", "Order not found or access denied.");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }

	        UserOrders userOrder = orderOptional.get();

	        // Get buyer details
	        Optional<User> buyerOptional = userRepository.findById(userOrder.getBuyerId());
	        if (buyerOptional.isEmpty()) {
	            response.put("error", "Buyer not found.");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	        }

	        User buyer = buyerOptional.get();

	        Map<String, Object> buyerData = new HashMap<>();
	        buyerData.put("orderId", userOrder.getRazorpayOrderId());
	        buyerData.put("name", buyer.getName());
	        buyerData.put("email", buyer.getEmail());

	        if (buyer.getProfile() != null) {
	            buyerData.put("phone", buyer.getProfile().getPhoneNumber());
	            buyerData.put("address", buyer.getProfile().getAddress());
	            buyerData.put("city", buyer.getProfile().getCity());
	            buyerData.put("state", buyer.getProfile().getState());
	            buyerData.put("country", buyer.getProfile().getCountry());
	            buyerData.put("pincode", buyer.getProfile().getPincode());
	        } else {
	            buyerData.put("phone", "");
	            buyerData.put("address", "");
	            buyerData.put("city", "");
	            buyerData.put("state", "");
	            buyerData.put("country", "");
	            buyerData.put("pincode", "");
	        }

	        // Get order items
	        List<OrderItems> orderItemsList = orderItemsRepository.findOrderItemByOderId(id);
	        List<Map<String, Object>> orderItems = new ArrayList<>();

	        for (OrderItems item : orderItemsList) {
	            Map<String, Object> itemData = new HashMap<>();
	            Optional<ProductPost> productPostOpt = productPostRepository.findById(item.getProductPostId());
	            Optional<Product> productOpt = productPostOpt.isPresent()
	                ? productRepository.findById(productPostOpt.get().getProductId())
	                : Optional.empty();

	            if (productPostOpt.isPresent() && productOpt.isPresent()) {
	                ProductPost productPost = productPostOpt.get();
	                Product product = productOpt.get();

	                itemData.put("name", productPost.getProductName());
	                itemData.put("quantity", item.getLotsQuntity());
	                itemData.put("amount", item.getLotPrice());

	                String productImage = product.getMainImage() != null
	                        ? "data:image/jpeg;base64," + Base64.getEncoder().encodeToString((byte[]) product.getMainImage())
	                        : "";

	                itemData.put("productImage", productImage);
	                itemData.put("total", item.getSubTotal());

	                orderItems.add(itemData);
	            }
	        }

	        // Prepare final response
	        response.put("buyerData", buyerData);
	        response.put("orderItems", orderItems);
	        response.put("finalAmount", userOrder.getFinalAmount());

	        return ResponseEntity.ok(response);

	    } catch (Exception ex) {
	        response.put("error", "Something went wrong. Please try again later.");
	        response.put("details", ex.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

}
