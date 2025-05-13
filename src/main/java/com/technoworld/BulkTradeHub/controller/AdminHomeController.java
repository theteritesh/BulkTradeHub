package com.technoworld.BulkTradeHub.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.technoworld.BulkTradeHub.entity.Brand;
import com.technoworld.BulkTradeHub.entity.Category;
import com.technoworld.BulkTradeHub.entity.Contact;
import com.technoworld.BulkTradeHub.entity.Product;
import com.technoworld.BulkTradeHub.entity.ProductPost;
import com.technoworld.BulkTradeHub.entity.Profile;
import com.technoworld.BulkTradeHub.entity.RetailShopProfile;
import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.repository.BrandRepository;
import com.technoworld.BulkTradeHub.repository.CategoryRepository;
import com.technoworld.BulkTradeHub.repository.UserRepository;
import com.technoworld.BulkTradeHub.service.AdminServiceImplementation;
import com.technoworld.BulkTradeHub.service.ContactService;
import com.technoworld.BulkTradeHub.service.UserService;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminHomeController {

	@Autowired
	private AdminServiceImplementation sr;

	@Autowired
	private ContactService contactService;

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private BrandRepository brandRepository;
	
	@GetMapping("/dashboard")
	public String adminDashboard(Model model) {
		
		return "admin/adminDashboard";
		
	}

	@GetMapping("/userManagement")
	public String userManagementPage() {
		return "admin/UserManagement";
	}

	@GetMapping("/getAllUserData")
	public String getAllUserData(Model model) {		
		List<User> user=sr.getAllUser();
		model.addAttribute("userData", user);
		return "admin/UserData";
	}

	// Edit user by id

	@GetMapping("/editUserById/{id}")
	public String editUserById(@PathVariable int id, Model model) {
		User userData = userService.findUserById(id);
		System.out.println(userData);
		model.addAttribute("data", userData);
		return "admin/editUser";
	}

	@PostMapping("/saveEditUser")
	public String updateUserSave(User user) {
		userService.saveEditUser(user);
		return "redirect:/admin/getAllUserData";
	}

	@GetMapping("/deleteUserById/{id}")
	public String deleteUserById(@PathVariable int id) {
		userService.deleteUserById(id);
		return "redirect:/admin/getAllUserData";
	}

	// Category Request Mapping

	@GetMapping("/category")
	public String categoryView(Model model) {

		List<Category> category = categoryRepository
				.findByStatusIn(List.of("Pending Approval", "Edit Requested", "Deletion Requested"));
		model.addAttribute("categoryData", category);
		return "admin/categoryRequest";
	}

	@GetMapping("/approveCategoryRequest/{id}")
	public String updateCategoryStatus(@PathVariable int id) {
		sr.updateCategoryById(id);
		return "redirect:/admin/category";
	}

	@GetMapping("/rejectCategoryRequest/{id}")
	public String rejectCategoryRequest(@PathVariable int id) {
		sr.rejectCategoryRequest(id);
		return "redirect:/admin/category";
	}

	// Brand Request Mapping

	@GetMapping("/brand")
	public String barndView(Model model) {
		List<Brand> brand = brandRepository
				.findByStatusIn(List.of("Pending Approval", "Edit Requested", "Deletion Requested"));
		model.addAttribute("brandData", brand);
		return "admin/brandRequest";
	}

	@GetMapping("/approveBrandRequest/{id}")
	public String updateBrandById(@PathVariable int id) {
		sr.updateBrandById(id);
		return "redirect:/admin/brand";
	}

	@GetMapping("/rejectBrandRequest/{id}")
	public String rejectBarndRequest(@PathVariable int id) {
		sr.rejectBarndRequest(id);
		return "redirect:/admin/brand";
	}

	// Get All Contact Message
	@GetMapping("/contactMessages")
	public String getAllContactMessage(Model model) {
		List<Contact> lst = contactService.getAllContactMessage();
		model.addAttribute("messageData", lst);
		return "/admin/contactMessage";
	}

	@GetMapping("/deleteConatctMessage/{id}")
	public String deleteConatctMessage(@PathVariable int id) {
		contactService.deleteConatctMessage(id);
		return "redirect:/admin/contactMessages";
	}

	@GetMapping("/getAllRetailer")
	public String retailerData(Model model) {
		List<User> user = sr.findAllRetailer();
		System.out.println(user);
		model.addAttribute("user_Retailer", user);
		return "admin/RetailerManagement";
	}

	@GetMapping("/categoryById/{id}")
	public String findCategoryByUserId(@PathVariable int id, Model model) {
		List<Category> categoryByUserId = sr.getCategoryByUserId(id);
		model.addAttribute("user", categoryByUserId);
		return "admin/userCategoryData";
	}

	@GetMapping("/deleteCategoryUserById/{id}")
	public String deleteCategoryByUserId(@PathVariable int id) {
		sr.deleteCategoryByUserId(id);
		return "redirect:/admin/getAllRetailer";
	}

	@GetMapping("/brandById/{id}")
	public String findBrandByUserId(@PathVariable int id, Model model) {
		List<Brand> brandByUserId = sr.getBrandByUserId(id);
		model.addAttribute("user", brandByUserId);
		return "admin/userBrandData";
	}

	@GetMapping("/deleteBrandUserById/{id}")
	public String deleteBrandUserByUserId(@PathVariable int id) {
		sr.deleteBrandByUserId(id);
		return "redirect:/admin/getAllRetailer";
	}

	@GetMapping("/getAllProductByUserId/{id}")
	public String getAllProductByUserId(@PathVariable int id, Model model) {
		List<Product> product = sr.getProductByUserId(id);
		model.addAttribute("user_product", product);
		return "admin/userProductData";
	}

	@GetMapping("/deleteProductUserById/{id}")
	public String deleteProductUserById(@PathVariable long id) {
		sr.deleteProductByUserId(id);
		return "redirect:/admin/getAllRetailer";
	}


	
	
	@GetMapping("/getAllSalesman")
	public String salemansData(Model model) {
		List<User> user = sr.findAllSalemans();
		System.out.println(user);
		model.addAttribute("user_Retailer", user);
		return "admin/SalesmanManagement";
	}
	
	@GetMapping("/getAllProducts")
	public String getMethodName(Model model) {
		
		List<Product> allProduct=sr.getAllProduct();
		model.addAttribute("products", allProduct);
		return "admin/allProducts";
	}
	
	@GetMapping("/postedProducts")
	public String postedProducts(Model model) {
		
		List<ProductPost> postProduct=sr.postedProducts();
		model.addAttribute("products",postProduct);
		return "admin/postedProduct";
	}
	
	@GetMapping("/deleteProduct/{id}")
	public String deleteProduct(@PathVariable long id)
	{
	sr.deleteProduct(id);
	return "redirect:/admin/getAllProducts";
	}

}
