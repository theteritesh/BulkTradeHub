package com.technoworld.BulkTradeHub.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.technoworld.BulkTradeHub.entity.Brand;
import com.technoworld.BulkTradeHub.entity.Category;
import com.technoworld.BulkTradeHub.entity.Product;
import com.technoworld.BulkTradeHub.entity.ProductPost;
import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.repository.BrandRepository;
import com.technoworld.BulkTradeHub.repository.CartRepository;
import com.technoworld.BulkTradeHub.repository.CategoryRepository;
import com.technoworld.BulkTradeHub.repository.ProductPostRepository;
import com.technoworld.BulkTradeHub.repository.ProductRepository;

@Controller
@RequestMapping("/retailShop")
public class RetailController {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private BrandRepository brandRepository;
	
	@Autowired
	private ProductPostRepository productPostRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@PostMapping("/requestCategory")
	public String requestCategory(@RequestParam("name") String name, Principal principal, RedirectAttributes redirectAttributes) {
	    User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

	    try {
	        if (name != null && !name.isEmpty()) {
	            // Check if the category already exists for this user
	            boolean exists = categoryRepository.existsByNameAndUser(name, user.getId());
	            if (exists) {
	                redirectAttributes.addFlashAttribute("errorMessage", "Category already exists!");
	                return "redirect:/dashboard/addCategory";
	            }

	            // Save new category
	            Category category = new Category();
	            category.setName(name);
	            category.setStatus("Pending Approval");
	            category.setUser(user.getId());
	            category.setCreatedAt(LocalDateTime.now());
	            categoryRepository.save(category);

	            redirectAttributes.addFlashAttribute("successMessage", "Request sent successfully!");
	        } else {
	            redirectAttributes.addFlashAttribute("errorMessage", "Category name cannot be empty!");
	        }
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Failed to send request.");
	    }

	    return "redirect:/dashboard/addCategory";
	}
	
	@PostMapping("/updateCategory")
	public String updateCategory(@RequestParam("id") int id, @RequestParam("name") String name, Principal principal, RedirectAttributes redirectAttributes) {
	    User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

	    try {
	        Optional<Category> categoryOptional = categoryRepository.findById(id);

	        if (categoryOptional.isPresent()) {
	            Category category = categoryOptional.get();

	            // Check if the edited category name already exists for this user
	            boolean exists = categoryRepository.existsByNameAndUser(name, user.getId());
	            if (exists) {
	                redirectAttributes.addFlashAttribute("errorMessage", "Category with this name already exists!");
	                return "redirect:/dashboard/addCategory";
	            }

	            // Update category status to "Pending" so the admin can review it
	            category.setName(name);
	            category.setStatus("Edit Requested");
	            category.setUpdatedAt(LocalDateTime.now());
	            categoryRepository.save(category);

	            redirectAttributes.addFlashAttribute("successMessage", "Edit request sent successfully! Awaiting admin approval.");
	        } else {
	            redirectAttributes.addFlashAttribute("errorMessage", "Category not found!");
	        }
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Failed to send edit request.");
	    }

	    return "redirect:/dashboard/addCategory";
	}


	@PostMapping("/deleteCategory")
	public String deleteCategory(@RequestParam("id") int id, Principal principal, RedirectAttributes redirectAttributes) {
	    User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

	    try {
	        Optional<Category> categoryOptional = categoryRepository.findById(id);

	        if (categoryOptional.isPresent()) {
	            Category category = categoryOptional.get();

	            // Ensure the category belongs to the logged-in user
	            if (category.getUser()!=user.getId()) {
	                redirectAttributes.addFlashAttribute("errorMessage", "Unauthorized request!");
	                return "redirect:/dashboard/addCategory";
	            }

	            // Update category status to "Deletion Requested"
	            category.setStatus("Deletion Requested");
	            category.setUpdatedAt(LocalDateTime.now());
	            categoryRepository.save(category);

	            redirectAttributes.addFlashAttribute("successMessage", "Deletion request sent successfully! Awaiting admin approval.");
	        } else {
	            redirectAttributes.addFlashAttribute("errorMessage", "Category not found!");
	        }
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Failed to send deletion request.");
	    }

	    return "redirect:/dashboard/addCategory";
	}
	
	
	
	@PostMapping("/requestBrand")
	public String requestBrand(@RequestParam("name") String name, Principal principal, RedirectAttributes redirectAttributes) {
	    User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

	    try {
	        if (name != null && !name.isEmpty()) {
	            // Check if the category already exists for this user
	            boolean exists = brandRepository.existsByNameAndUser(name, user.getId());
	            if (exists) {
	                redirectAttributes.addFlashAttribute("errorMessage", "Brand already exists!");
	                return "redirect:/dashboard/addBrand";
	            }

	            // Save new category
	            Brand brand= new Brand();
	            brand.setName(name);
	            brand.setStatus("Pending Approval");
	            brand.setUser(user.getId());
	            brand.setCreatedAt(LocalDateTime.now());
	            brandRepository.save(brand);

	            redirectAttributes.addFlashAttribute("successMessage", "Request sent successfully!");
	        } else {
	            redirectAttributes.addFlashAttribute("errorMessage", "Brand name cannot be empty!");
	        }
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Failed to send request.");
	    }

	    return "redirect:/dashboard/addBrand";
	}
	
	@PostMapping("/updateBrand")
	public String updateBrand(@RequestParam("id") int id, @RequestParam("name") String name, Principal principal, RedirectAttributes redirectAttributes) {
	    User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

	    try {
	        Optional<Brand> brandOptional = brandRepository.findById(id);

	        if (brandOptional.isPresent()) {
	            Brand brand = brandOptional.get();

	            // Check if the edited category name already exists for this user
	            boolean exists = brandRepository.existsByNameAndUser(name, user.getId());
	            if (exists) {
	                redirectAttributes.addFlashAttribute("errorMessage", "Brand with this name already exists!");
	                return "redirect:/dashboard/addBrand";
	            }

	            // Update category status to "Pending" so the admin can review it
	            brand.setName(name);
	            brand.setStatus("Edit Requested");
	            brand.setUpdatedAt(LocalDateTime.now());
	            brandRepository.save(brand);

	            redirectAttributes.addFlashAttribute("successMessage", "Edit request sent successfully! Awaiting admin approval.");
	        } else {
	            redirectAttributes.addFlashAttribute("errorMessage", "Brand not found!");
	        }
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Failed to send edit request.");
	    }

	    return "redirect:/dashboard/addBrand";
	}


	@PostMapping("/deleteBrand")
	public String deleteBrand(@RequestParam("id") int id, Principal principal, RedirectAttributes redirectAttributes) {
	    User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

	    try {
	        Optional<Brand> brandOptional = brandRepository.findById(id);

	        if (brandOptional.isPresent()) {
	            Brand brand = brandOptional.get();

	            // Ensure the category belongs to the logged-in user
	            if (brand.getUser()!=user.getId()) {
	                redirectAttributes.addFlashAttribute("errorMessage", "Unauthorized request!");
	                return "redirect:/dashboard/addBrand";
	            }

	            // Update category status to "Deletion Requested"
	            brand.setStatus("Deletion Requested");
	            brand.setUpdatedAt(LocalDateTime.now());
	           brandRepository.save(brand);

	            redirectAttributes.addFlashAttribute("successMessage", "Deletion request sent successfully! Awaiting admin approval.");
	        } else {
	            redirectAttributes.addFlashAttribute("errorMessage", "Brand not found!");
	        }
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("errorMessage", "Failed to send deletion request.");
	    }

	    return "redirect:/dashboard/addBrand";
	}
	
	@GetMapping("/searchPost")
    public String searchProducts(@RequestParam("query") String query, Model model,Principal principal) {
    	User user =  (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        List<ProductPost> filteredProductPosts = productPostRepository.searchProductPost(query,user.getId());
        model.addAttribute("productPostList", filteredProductPosts);
        model.addAttribute("query", query);
        return "/retailshop/showPost";
    }
	
	@GetMapping("/deleteProductPost/{id}")
    public String deleteProductPost(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
        	Optional<ProductPost> productPostOptional=productPostRepository.findById(id);
        	if(productPostOptional.isPresent()) {
        		productPostRepository.delete(productPostOptional.get());
        	}
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete product.");
        }
        return "redirect:/dashboard/showPost";
    }
    
	 @GetMapping("/searchLowCostProduct")
	    public String searchLowCostProduct(@RequestParam("query") String query, Model model,Principal principal) {
	    	User user =  (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
	        List<Product> filteredProducts = productRepository.searchLowStockProductsByUser(query,user.getId());
	        model.addAttribute("products", filteredProducts);
	        model.addAttribute("query", query);
	        return "/retailshop/lowStockProduct"; 
	    }
	 
	 @PostMapping("/updateQuantity")
	    public String updateQuantity(@RequestParam Long id,
	                                @RequestParam int totalQuantity,
	                                RedirectAttributes redirectAttributes) {
	        Optional<Product> existingProductOptional = productRepository.findById(id);
			Product existingProduct=existingProductOptional.get();
			if (!existingProductOptional.isPresent()) {
			    redirectAttributes.addFlashAttribute("errorMessage", "Product not found!");
			    return "redirect:/dashboard/lowStockProduct";
			}
			existingProduct.setTotalQuantity(totalQuantity);
			// Save the updated product
			productRepository.save(existingProduct);
			redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");

	        return "redirect:/dashboard/showLowStockProduct";
	    }
	 
	 @GetMapping("/retailDashboardOverview")
	 @ResponseBody
	 public Map<String, Long> getRetailDashboardOverview(Principal principal) {
	     User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
	     Map<String, Long> status = new HashMap<>();

	     status.put("totalPosted", Optional.ofNullable(productPostRepository.countTotalProductsByUser(user)).orElse(0L));
	     status.put("outOfStock", Optional.ofNullable(productRepository.countOutOfStockProductsByUser(user)).orElse(0L));
	     status.put("lowStock", Optional.ofNullable(productRepository.countLowStockProductsByUser(user)).orElse(0L));
	     status.put("inStock", Optional.ofNullable(productRepository.countByUser(user)).orElse(0L));

	     return status;
	 }
	 
	 @GetMapping("/monthlyProductCount")
	 @ResponseBody
	 public List<Map<String, Object>> getMonthlyProductCountByUser(Principal principal) {
		    User user =  (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
	        int currentYear = LocalDate.now().getYear();

	        List<Object[]> results = productRepository.getMonthlyProductCountByUser(currentYear, user.getId());

	        List<Map<String, Object>> response = new ArrayList<>();
	        for (Object[] row : results) {
	            Map<String, Object> data = new HashMap<>();
	            data.put("month", row[0]);
	            data.put("count", row[1]);
	            response.add(data);
	        }

	        return response;
	    }


}
