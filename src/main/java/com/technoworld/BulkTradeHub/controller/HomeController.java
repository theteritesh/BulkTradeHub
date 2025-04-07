package com.technoworld.BulkTradeHub.controller;

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
}
