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
import org.springframework.web.bind.annotation.RequestParam;

import com.technoworld.BulkTradeHub.entity.Brand;
import com.technoworld.BulkTradeHub.entity.Category;
import com.technoworld.BulkTradeHub.entity.Product;
import com.technoworld.BulkTradeHub.entity.ProductPost;
import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.repository.BrandRepository;
import com.technoworld.BulkTradeHub.repository.CategoryRepository;
import com.technoworld.BulkTradeHub.repository.ProductRepository;
import com.technoworld.BulkTradeHub.service.ProductService;


@Controller
@RequestMapping("/dashboard")
public class DashboardController {
	
	private final ProductService productService;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private BrandRepository brandRepository;
	
	
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
	
	@GetMapping("/addCategory")
	public String displayCategory(@RequestParam(value = "query", required = false) String query,
	                              Model model,
	                              @ModelAttribute("successMessage") String successMessage,
	                              @ModelAttribute("errorMessage") String errorMessage,
	                              Principal principal) {
	    User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

	    List<Category> categories;
	    if (query != null && !query.isEmpty()) {
	        categories = categoryRepository.findByNameContainingAndUser(query, user.getId());
	    } else {
	        categories = categoryRepository.findByUser(user.getId());
	    }

	    model.addAttribute("categoriesList", categories);
	    model.addAttribute("category", new Category());
	    model.addAttribute("successMessage", successMessage);
	    model.addAttribute("errorMessage", errorMessage);
	    model.addAttribute("query", query); // Preserve search input

	    return "/retailshop/category";
	}
	
	@GetMapping("/addBrand")
	public String displayBrand(@RequestParam(value = "query", required = false) String query,
	                              Model model,
	                              @ModelAttribute("successMessage") String successMessage,
	                              @ModelAttribute("errorMessage") String errorMessage,
	                              Principal principal) {
	    User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

	    List<Brand> brands;
	    if (query != null && !query.isEmpty()) {
	    	brands = brandRepository.findByNameContainingAndUser(query, user.getId());
	    } else {
	    	brands = brandRepository.findByUser(user.getId());
	    }

	    model.addAttribute("brandList", brands);
	    model.addAttribute("brand", new Brand());
	    model.addAttribute("successMessage", successMessage);
	    model.addAttribute("errorMessage", errorMessage);
	    model.addAttribute("query", query); // Preserve search input

	    return "/retailshop/brand";
	}



}
