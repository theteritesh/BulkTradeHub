package com.technoworld.BulkTradeHub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.technoworld.BulkTradeHub.entity.Profile;
import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.repository.UserRepository;
import com.technoworld.BulkTradeHub.retailshop.entity.RetailShopProfile;
import com.technoworld.BulkTradeHub.retailshop.repository.ProductRepository;
import com.technoworld.BulkTradeHub.retailshop.repository.RetailShopProfileRepository;
import com.technoworld.BulkTradeHub.service.AdminServiceImplementation;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {
	
@Autowired
private AdminServiceImplementation sr;
	

	@GetMapping("/dashboard")
	public String adminDashboard()
	{
		return"admin/adminDashboard";
	}
	@GetMapping("/userManagement")
	public String userManagementPage() {
		return "admin/UserManagement";
	}
	
	@GetMapping("/getAllUserData")
	public String userData(Model model) {		
		List<User> user=sr.getAllUser();
		
		System.out.println(user);
		model.addAttribute("userData", user);
		return "admin/UserData";
	}
	
	@GetMapping("/getAllRetailer")
	public String retailerData(Model model) {
		List<RetailShopProfile> retailerData=sr.getAllRetailShopProfileData();
		model.addAttribute("RD", retailerData);
		return "admin/RetailerManagement";
	}

	/*
	 * @GetMapping("/getAllSealsMan") public String sealsManData() {
	 * 
	 * List<> sealsmanData=sr.getAllSalesmanData(); return
	 * "admin/SealsManManagement"; }
	 */

}
