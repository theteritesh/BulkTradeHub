package com.technoworld.BulkTradeHub.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.technoworld.BulkTradeHub.entity.Cart;
import com.technoworld.BulkTradeHub.entity.Contact;
import com.technoworld.BulkTradeHub.entity.Product;
import com.technoworld.BulkTradeHub.entity.ProductPost;
import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.repository.CartRepository;
import com.technoworld.BulkTradeHub.repository.ProductPostRepository;
import com.technoworld.BulkTradeHub.repository.ProductRepository;
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

}
