package com.technoworld.BulkTradeHub.retailshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.technoworld.BulkTradeHub.retailshop.entity.Product;


@Controller
@RequestMapping("/dashboard")
public class DashboardController {
	
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
}
