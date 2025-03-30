package com.technoworld.BulkTradeHub.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.technoworld.BulkTradeHub.entity.Product;
import com.technoworld.BulkTradeHub.entity.ProductPost;
import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.repository.ProductRepository;
import com.technoworld.BulkTradeHub.service.ProductService;


@Controller
@RequestMapping("/dashboard")
public class DashboardController {
	
	private final ProductService productService;
	
	@Autowired
	private ProductRepository productRepository;
	
	
	public DashboardController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("")
	public String displayDashboard(Model model) {
		return "/retailshop/dashboard";
	}
	
	@GetMapping({"/addProduct"})
	public String displayAddProduct(Model model,@ModelAttribute("successMessage") String successMessage) {
		model.addAttribute("product",new Product());
	    model.addAttribute("successMessage", successMessage);
		return "/retailshop/addProduct";
	}
	
	@GetMapping("/showProducts")
    public String displayProducts(Model model,@ModelAttribute("successMessage") String successMessage,Principal principal) {
		User user =  (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        List<Product> productList = productService.getAllProducts(user);
        model.addAttribute("successMessage", successMessage);
        model.addAttribute("products", productList);
        return "/retailshop/showProduct";
    }
	
	@GetMapping("/postProduct")
	public String postProducts(Model model,Principal principal) {
		User user =  (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
	    Product firstProduct = productRepository.findFirstByUserOrderByIdDesc(user);
	    ProductPost productPost = new ProductPost();

	    if (firstProduct != null) {
	        productPost.setProductName(firstProduct.getName());
	        productPost.setCategory(firstProduct.getCategory());
	        productPost.setBrand(firstProduct.getBrand());
	        productPost.setDescription(firstProduct.getDescription());
	        productPost.setRetailPrice(firstProduct.getPrice());
	        productPost.setAvailableQuantity(firstProduct.getTotalQuantity());
	        productPost.setWholesalePrice(firstProduct.getPrice());
	        productPost.setProductId(firstProduct.getId());

	        model.addAttribute("productId", firstProduct.getId());
	    } else {
	        model.addAttribute("errorMessage", "Please add a product first.");
	    }

	    model.addAttribute("PostProduct", productPost);

	    return "/retailshop/postProduct";
	}

}
