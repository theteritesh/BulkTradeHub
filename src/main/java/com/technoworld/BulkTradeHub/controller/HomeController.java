package com.technoworld.BulkTradeHub.controller;

import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.technoworld.BulkTradeHub.entity.Contact;
import com.technoworld.BulkTradeHub.entity.Product;
import com.technoworld.BulkTradeHub.entity.ProductPost;
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
    public String displayHome(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 16;  // Show 16 products per page
        Page<ProductPost> newArrivalProductsPage = productPostRepository.findAllByOrderByPostedAtDesc(PageRequest.of(page, pageSize));

        model.addAttribute("newArrivalProductsPage", newArrivalProductsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", newArrivalProductsPage.getTotalPages());

        return "index";
    }
	
	@GetMapping("/product/image/{id}")
	@ResponseBody
	public ResponseEntity<byte[]> getDefaultProductImage(@PathVariable Long id) {
	    Optional<Product> productOptional = productRepository.findById(id);

	    if (productOptional.isPresent()) {
	        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(productOptional.get().getMainImage());
	    }

	    return ResponseEntity.notFound().build();
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
	    
	    int pageSize = 4; 
        Page<ProductPost> newArrivalProductsPage = productPostRepository.findAllByOrderByPostedAtDesc(PageRequest.of(page, pageSize));
        Page<ProductPost> relatedProductsPage = productPostRepository.findByCategoryOrderByPostedAtDesc(productPost.getCategory(),PageRequest.of(page, pageSize));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
        String formattedPostedAt = productPost.getPostedAt().format(formatter);
        
        model.addAttribute("formattedPostedAt", formattedPostedAt);
        model.addAttribute("relatedProductsPage", relatedProductsPage);
        model.addAttribute("newArrivalProductsPage", newArrivalProductsPage);
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

}
