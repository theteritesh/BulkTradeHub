package com.technoworld.BulkTradeHub.retailshop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.technoworld.BulkTradeHub.retailshop.entity.Product;
import com.technoworld.BulkTradeHub.retailshop.service.ProductService;


@Controller
@RequestMapping("/dashboard")
public class DashboardController {
	
	private final ProductService productService;
	
	
	public DashboardController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("")
	public String displayDashboard() {
		return "/retailshop/dashboard";
	}
	
	@GetMapping({"/addProduct"})
	public String displayAddProduct(Model model,@ModelAttribute("successMessage") String successMessage) {
		model.addAttribute("product",new Product());
	    model.addAttribute("successMessage", successMessage);
		return "/retailshop/addProduct";
	}
	
	@GetMapping("/showProducts")
    public String displayProducts(Model model) {
        List<Product> productList = productService.getAllProducts();
        model.addAttribute("products", productList);
        return "/retailshop/showProduct";
    }
}
