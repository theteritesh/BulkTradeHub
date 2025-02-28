package com.technoworld.BulkTradeHub.retailshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
	
	@RequestMapping(value={""})
	public String displayDashboard() {
		return "dashboard";
	}
	
	@RequestMapping(value= {"/addProduct"})
	public String displayAddProduct() {
		return "addProduct";
	}

}
