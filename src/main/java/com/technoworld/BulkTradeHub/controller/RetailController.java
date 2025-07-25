package com.technoworld.BulkTradeHub.controller;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.technoworld.BulkTradeHub.entity.Brand;
import com.technoworld.BulkTradeHub.entity.Category;
import com.technoworld.BulkTradeHub.entity.Product;
import com.technoworld.BulkTradeHub.entity.ProductGtinInfo;
import com.technoworld.BulkTradeHub.entity.ProductPost;
import com.technoworld.BulkTradeHub.entity.RetailShopProfile;
import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.repository.BrandRepository;
import com.technoworld.BulkTradeHub.repository.CartRepository;
import com.technoworld.BulkTradeHub.repository.CategoryRepository;
import com.technoworld.BulkTradeHub.repository.ProductGtinInfoRepository;
import com.technoworld.BulkTradeHub.repository.ProductPostRepository;
import com.technoworld.BulkTradeHub.repository.ProductRepository;
import com.technoworld.BulkTradeHub.repository.ProfileRepository;
import com.technoworld.BulkTradeHub.repository.RetailShopProfileRepository;
import com.technoworld.BulkTradeHub.service.ProductService;

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
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProfileRepository profileRepository;
	
	@Autowired
	private RetailShopProfileRepository retailShopProfileRepository;
	
	@Autowired
	private ProductGtinInfoRepository productGtinInfoRepository;
	
	@GetMapping("/profile")
    public String getProfilePage(Model model,Principal principal) {
        User user =  (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        
        RetailShopProfile retailShopProfile=(RetailShopProfile)profileRepository.findByUser(user); 
        
        if (retailShopProfile == null) {
            retailShopProfile = new RetailShopProfile();
        } else {
            if (retailShopProfile.getPaymentMethods() != null && !retailShopProfile.getPaymentMethods().isEmpty()) {
            	retailShopProfile.setPaymentMethodsList(Arrays.asList(retailShopProfile.getPaymentMethods().split(",")));
            }else {
            	retailShopProfile.setPaymentMethodsList(new ArrayList<String>());
            }
            retailShopProfile.setProfileImg(retailShopProfile.getProfileImg());
        }
        
        model.addAttribute("retailShopProfile", retailShopProfile);
        return "retailshop/profile";
    }
    
    @GetMapping("/profileImg")
    @ResponseBody
    public ResponseEntity<byte[]> getProfileImage(Principal principal) {
        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        RetailShopProfile retailShopProfile = (RetailShopProfile) profileRepository.findByUser(user);

        if (retailShopProfile != null && retailShopProfile.getProfileImg() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(retailShopProfile.getProfileImg());
        }

        try {
            InputStream defaultImageStream = getClass().getResourceAsStream("/static/retailshop/img/default.png");
            if (defaultImageStream != null) {
                byte[] defaultImageBytes = defaultImageStream.readAllBytes();
                return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(defaultImageBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.notFound().build();
    }



    @PostMapping("/profile/save")
    public String saveProfile(
            @RequestParam String shopName,
            @RequestParam String shopType,
            @RequestParam String gstNumber,
            @RequestParam String phoneNumber,
            @RequestParam String address,
            @RequestParam String city,
            @RequestParam String state,
            @RequestParam String country,
            @RequestParam String pincode,
            @RequestParam(required = false) String bankAccountHolderName,
            @RequestParam(required = false) String bankAccountNumber,
            @RequestParam(required = false) String bankName,
            @RequestParam(required = false) String ifscCode,
            @RequestParam(required = false) String upiId,
            @RequestParam(required = false) List<String> paymentMethodsList,
            @RequestParam(required = false) MultipartFile profileImg,
            Principal principal) throws IOException {
    	
    	User user =  (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
    	RetailShopProfile retailShopProfile=(RetailShopProfile) profileRepository.findByUser(user);
        
        if (retailShopProfile == null) {
        	retailShopProfile = new RetailShopProfile();
        	retailShopProfile.setUser(user);
        }
        
        retailShopProfile.setShopName(shopName);
        retailShopProfile.setShopType(shopType);
        retailShopProfile.setGstNumber(gstNumber);
        retailShopProfile.setPhoneNumber(phoneNumber);
        retailShopProfile.setAddress(address);
        retailShopProfile.setCity(city);
        retailShopProfile.setState(state);
        retailShopProfile.setCountry(country);
        retailShopProfile.setPincode(pincode);
        retailShopProfile.setBankAccountHolderName(bankAccountHolderName);
        retailShopProfile.setBankAccountNumber(bankAccountNumber);
        retailShopProfile.setBankName(bankName);
        retailShopProfile.setIfscCode(ifscCode);
        retailShopProfile.setUpiId(upiId);
        if(paymentMethodsList == null) {
        	retailShopProfile.setPaymentMethods(null);
        }else {
        	retailShopProfile.setPaymentMethods(String.join(",", paymentMethodsList));
        }
        

        if(profileImg !=null && !profileImg.isEmpty()) {
        	retailShopProfile.setProfileImg(profileImg.getBytes());
        }
        
        retailShopProfileRepository.save(retailShopProfile);

        return "redirect:/retailShop/profile?success";
    }
	
	@GetMapping("/dashboard")
	public String displayDashboard(Model model) {
		return "/retailshop/dashboard";
	}
	
	@GetMapping({"/addProduct"})
	public String displayAddProduct(Model model,@ModelAttribute("successMessage") String successMessage,
			Principal principal) {
		User user =  (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
		List<Category> categories=categoryRepository.findByUserAndStatusIn(user.getId(),List.of("Active", "Edit Approved"));
		List<Brand> brandsList=brandRepository.findByUserAndStatusIn(user.getId(),List.of("Active", "Edit Approved"));
		
		model.addAttribute("categoryList", categories);
		model.addAttribute("brandList",brandsList );
		model.addAttribute("product",new Product());
	    model.addAttribute("successMessage", successMessage);
		return "/retailshop/addProduct";
	}
	
	@GetMapping("/showProducts")
    public String displayProducts(Model model,@ModelAttribute("successMessage") String successMessage,Principal principal) {
		User user =  (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        List<Product> productList = productService.getAllProducts(user);
        
		List<Category> categories=categoryRepository.findByUserAndStatusIn(user.getId(),List.of("Active", "Edit Approved"));
		List<Brand> brandsList=brandRepository.findByUserAndStatusIn(user.getId(),List.of("Active", "Edit Approved"));
		
		model.addAttribute("categoryList", categories);
		model.addAttribute("brandList",brandsList );
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
	        categories = categoryRepository.findByUserAndStatusNot(user.getId(),"Deleted");
	    }

	    model.addAttribute("categoriesList", categories);
	    model.addAttribute("category", new Category());
	    model.addAttribute("successMessage", successMessage);
	    model.addAttribute("errorMessage", errorMessage);
	    model.addAttribute("query", query); 

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
	    	brands = brandRepository.findByUserAndStatusNot(user.getId(),"Deleted");
	    }

	    model.addAttribute("brandList", brands);
	    model.addAttribute("brand", new Brand());
	    model.addAttribute("successMessage", successMessage);
	    model.addAttribute("errorMessage", errorMessage);
	    model.addAttribute("query", query); // Preserve search input

	    return "/retailshop/brand";
	}
	
	@GetMapping("/showPost")
    public String displayPost(Model model,@ModelAttribute("successMessage") String successMessage,Principal principal) {
		User user =  (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        List<ProductPost> productPostList =productPostRepository.findAllByUserOrderByIdDesc(user);
        model.addAttribute("successMessage", successMessage);
        model.addAttribute("productPostList", productPostList);
        return "/retailshop/showPost";
    }

	@GetMapping("/showLowStockProduct")
    public String displayLowStockProduct(Model model,@ModelAttribute("successMessage") String successMessage,Principal principal) {
		User user =  (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        List<Product> productList = productRepository.findLowStockProductsByUser(user);
        model.addAttribute("successMessage", successMessage);
        model.addAttribute("products", productList);
        return "/retailshop/lowStockProduct";
    }
	
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
    public String searchPost(@RequestParam("query") String query, Model model,Principal principal) {
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
        return "redirect:/retailShop/showPost";
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

	 @PostMapping("/addProduct")
	    public String saveProduct(@RequestParam String name,
	    		@RequestParam String category,
	    		@RequestParam String brand,
	    		@RequestParam String description,
	    		@RequestParam String unitType,
	    		@RequestParam Double unitValue,
	    		@RequestParam Double price,
	    		@RequestParam Double cost,
	    		@RequestParam int totalQuantity,
	    		@RequestParam("mainImage") MultipartFile mainImage,
	    		@RequestParam("firstImage") MultipartFile firstImage,
	    		@RequestParam("secondImage") MultipartFile secondImage,
	    		@RequestParam("thirdImage") MultipartFile thirdImage,
	    		@RequestParam("fourthImage") MultipartFile fourthImage,
	    		@RequestParam("fifthImage") MultipartFile fifthImage,
	    		@RequestParam String gTin,
	    		@RequestParam String warranty,
	    		@RequestParam LocalDate expiryDate,
	    		
	    		RedirectAttributes redirectAttributes,
	    		Principal principal) {
	    	
	    	try {
	    		User user =  (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
		    	Product product=new Product();
		    	product.setName(name);
		    	product.setCategory(category);
		    	product.setBrand(brand);
		    	product.setDescription(description);
		    	product.setUnitType(unitType);
		    	product.setUnitValue(unitValue);
		    	product.setPrice(price);
		    	product.setCost(cost);
		    	product.setTotalQuantity(totalQuantity);
		    	
		    	if(mainImage != null) {
		    		product.setMainImage(mainImage.getBytes());
		    	}else {
		    		product.setMainImage(null);
		    	}
		    	
		    	if(firstImage != null) {
		    		product.setFirstImage(firstImage.getBytes());
		    	}else {
		    		product.setFirstImage(null);
		    	}
		    	if(secondImage != null) {
		    		product.setSecondImage(secondImage.getBytes());
		    	}else {
		    		product.setSecondImage(null);
		    	}
		    	if(thirdImage != null) {
		    		product.setThirdImage(thirdImage.getBytes());
		    	}else {
		    		product.setThirdImage(null);
		    	}
		    	if(fourthImage != null) {
		    		product.setFourthImage(fourthImage.getBytes());
		    	}else {
		    		product.setFourthImage(null);
		    	}
		    	if(fifthImage != null) {
		    		product.setFifthImage(fifthImage.getBytes());
		    	}else {
		    		product.setFifthImage(null);
		    	}
		    	
		    	product.setUser(user);
		    	product.setExpiryDate(expiryDate);
		    	product.setWarranty(warranty);
		    	product.setgTin(gTin);
		    	product.setCreatedAt(LocalDateTime.now());
		    	
		        productRepository.save(product);
		        redirectAttributes.addFlashAttribute("successMessage", "Product added successfully!");
		    	
			} catch (IOException e) {
				e.printStackTrace();
				redirectAttributes.addFlashAttribute("errorMessage", "Failed to add product.");
			}
	    	
	    	return "redirect:/retailShop/addProduct";
	    }
	 
	 @GetMapping("/verifyGtin")
	    public ResponseEntity<Map<String, Object>> checkProduct(@RequestParam(required = false) String gtin) {
	        Map<String, Object> responseMap = new HashMap<>();

	        if (gtin == null || gtin.isEmpty()) {
	            responseMap.put("valid", false);
	            responseMap.put("message", "GTIN cannot be empty.");
	            return ResponseEntity.badRequest().body(responseMap);
	        }

	        // Try from local DB first
	        Optional<ProductGtinInfo> localProduct = productGtinInfoRepository.findByGtin(gtin);
	        if (localProduct.isPresent()) {
	            ProductGtinInfo product = localProduct.get();
	            responseMap.put("valid", true);
	            responseMap.put("product_name", product.getProductName());
	            responseMap.put("brand", product.getBrand());
	            responseMap.put("category", product.getCategory());
	            responseMap.put("description", product.getDescription());
	            return ResponseEntity.ok(responseMap);
	        }

	        // If API still available (optional fallback logic)
	        /*
	        String url = API_URL + "?formatted=y&key=" + API_KEY + "&barcode=" + gtin;
	        RestTemplate restTemplate = new RestTemplate();
	        try {
	            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
	            // Parse and use as before...
	        } catch (Exception e) {
	            responseMap.put("valid", false);
	            responseMap.put("message", "Error verifying GTIN: " + e.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
	        }
	        */

	        responseMap.put("valid", false);
	        responseMap.put("message", "Product not found in local database.");
	        return ResponseEntity.ok(responseMap);
	    }
	 
	 @GetMapping("/productImage/{imageType}/{id}")
	    @ResponseBody
	    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id, @PathVariable String imageType) {
	        Product product = productRepository.findById(id).orElse(null);;
	        
	        if (product != null) {
	            byte[] imageData = null;
	            
	            switch (imageType.toLowerCase()) {
	                case "mainimage":
	                    imageData = product.getMainImage();
	                    break;
	                case "firstimage":
	                    imageData = product.getFirstImage();
	                    break;
	                case "secondimage":
	                    imageData = product.getSecondImage();
	                    break;
	                case "thirdimage":
	                    imageData = product.getThirdImage();
	                    break;
	                case "fourthimage":
	                    imageData = product.getFourthImage();
	                    break;
	                case "fifthimage":
	                    imageData = product.getFifthImage();
	                    break;
	                default:
	                    return ResponseEntity.badRequest().build();
	            }

	            if (imageData != null) {
	                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
	            }
	        }
	        
	        return ResponseEntity.notFound().build();
	    }

	    @GetMapping("/deleteLowStockProduct/{id}")
	    public String deleteLowStockProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
	        try {
	            productService.deleteProductById(id);
	            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
	        } catch (Exception e) {
	            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete product.");
	        }
	        return "redirect:/retailShop/showLowStockProduct";
	    }
	    
	    @PostMapping("/postProductPost")
	    public String postProductPost(
	        @RequestParam("productId") Long productId, 
	        @RequestParam("description") String description,
	        @RequestParam("minOrderQuantity") int minOrderQuantity,
	        @RequestParam("bulkPackageType") String bulkPackageType,
	        @RequestParam("wholesalePrice") double wholesalePrice,
	        @RequestParam("bulkDiscount") double bulkDiscount,
	        @RequestParam("deliveryTime") String deliveryTime,
	        @RequestParam("shippingCost") double shippingCost,
	        @RequestParam("availabilityType") String availabilityType,
	        @RequestParam("leadTime") String leadTime,
	        @RequestParam("lots") int lots,
	        @RequestParam(value = "codAvailable", required = false) Boolean codAvailable,
	        RedirectAttributes redirectAttributes,Principal principal) {
	    	User user =  (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
	        // Find the Product by ID
	        Optional<Product> productOptional = productRepository.findById(productId);

	        if (productOptional.isPresent()) {
	            Product product = productOptional.get();

	            // Create a new ProductPost using the Product details
	            ProductPost productPost = new ProductPost();
	            productPost.setProductId(product.getId());
	            productPost.setProductName(product.getName());
	            productPost.setCategory(product.getCategory());
	            productPost.setBrand(product.getBrand());
	            productPost.setDescription(description);
	            productPost.setRetailPrice(product.getPrice());
	            productPost.setMinOrderQuantity(minOrderQuantity);
	            productPost.setBulkPackageType(bulkPackageType);
	            productPost.setWholesalePrice(wholesalePrice);
	            productPost.setBulkDiscount(bulkDiscount);
	            productPost.setDeliveryTime(deliveryTime);
	            productPost.setShippingCost(shippingCost);
	            productPost.setAvailabilityType(availabilityType);
	            productPost.setLeadTime(leadTime);
	            productPost.setCodAvailable(codAvailable != null ? codAvailable : false);
	            productPost.setPostedAt(LocalDateTime.now());
	            productPost.setAvailableQuantity(product.getTotalQuantity());
	            productPost.setUser(user);
	            productPost.setLots(lots);
	            
	            // Save ProductPost
	            productPostRepository.save(productPost);
	            
	            // Product quantity minus after post
	            product.setTotalQuantity(product.getTotalQuantity()-(minOrderQuantity*lots));
	            productRepository.save(product);

	            // Success message
	            redirectAttributes.addFlashAttribute("successMessage", "Product posted successfully!");
	            redirectAttributes.addFlashAttribute("productId", product.getId());
	        } else {
	            // If product not found
	            redirectAttributes.addFlashAttribute("errorMessage", "Product not found. Please try again.");
	        }

	        return "redirect:/retailShop/searchProductForPost?productId=" + productId;
	    }
	    
	    @GetMapping("/searchProductForPost")
	    public String searchProductForPost(@RequestParam(value = "productId", required = false) Long productId, Model model,
	    		Principal principal) {
	    	User user =  (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
	        ProductPost productPost = new ProductPost();

	        if (productId != null) {
	            Optional<Product> productOptional = productRepository.findByIdAndUser(productId,user.getId());
	            if (productOptional.isPresent()) {
	                Product product = productOptional.get();
	                productPost.setProductName(product.getName());
	                productPost.setCategory(product.getCategory());
	                productPost.setBrand(product.getBrand());
	                productPost.setDescription(product.getDescription());
	                productPost.setRetailPrice(product.getPrice());
	                productPost.setAvailableQuantity(product.getTotalQuantity());
	                productPost.setWholesalePrice(product.getPrice());
	    	        productPost.setProductId(product.getId());

	                model.addAttribute("productId", product.getId());
	            } else {
	                model.addAttribute("errorMessage", "Product not found. Please add a product first.");
	            }
	        } else {
	            model.addAttribute("errorMessage", "Please enter a Product ID to search.");
	        }

	        model.addAttribute("PostProduct", productPost); 

	        return "/retailshop/postProduct";
	    }
	    
	    @GetMapping("/searchProducts")
	    public String searchProducts(@RequestParam("query") String query, Model model,Principal principal) {
	    	User user =  (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
	        List<Product> filteredProducts = productService.searchProducts(query,user.getId());
	        model.addAttribute("products", filteredProducts);
	        model.addAttribute("query", query);
	        return "/retailshop/showProduct"; 
	    }
	    
	    @GetMapping("/deleteProduct/{id}")
	    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
	        try {
	            productService.deleteProductById(id);
	            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
	        } catch (Exception e) {
	            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete product.");
	        }
	        return "redirect:/retailShop/showProducts";
	    }
	    
	    @PostMapping("/updateProduct")
	    public String updateProduct(@RequestParam Long id,
	                                @RequestParam String name,
	                                @RequestParam String category,
	                                @RequestParam String brand,
	                                @RequestParam String description,
	                                @RequestParam String unitType,
	                                @RequestParam Double unitValue,
	                                @RequestParam Double price,
	                                @RequestParam Double cost,
	                                @RequestParam int totalQuantity,
	                        		@RequestParam("mainImage") MultipartFile mainImage,
	                        		@RequestParam("firstImage") MultipartFile firstImage,
	                        		@RequestParam("secondImage") MultipartFile secondImage,
	                        		@RequestParam("thirdImage") MultipartFile thirdImage,
	                        		@RequestParam("fourthImage") MultipartFile fourthImage,
	                        		@RequestParam("fifthImage") MultipartFile fifthImage,
	                        		@RequestParam String warranty,
	                        		@RequestParam LocalDate expiryDate,
	                                RedirectAttributes redirectAttributes) {
	        try {
	            Product existingProduct = productService.getProductById(id);
	            if (existingProduct == null) {
	                redirectAttributes.addFlashAttribute("errorMessage", "Product not found!");
	                return "redirect:/retailShop/showProducts";
	            }

	            // Update fields
	            existingProduct.setName(name);
	            existingProduct.setCategory(category);
	            existingProduct.setBrand(brand);
	            existingProduct.setDescription(description);
	            existingProduct.setUnitType(unitType);
	            existingProduct.setUnitValue(unitValue);
	            existingProduct.setPrice(price);
	            existingProduct.setCost(cost);
	            existingProduct.setTotalQuantity(totalQuantity);
	            existingProduct.setWarranty(warranty);
	            existingProduct.setExpiryDate(expiryDate);
	            existingProduct.setModifiedAt(LocalDateTime.now());

	            // Update image only if a new file is uploaded
	            if (mainImage != null && !mainImage.isEmpty()) {
	                existingProduct.setMainImage(mainImage.getBytes());
	            }
	            if (firstImage != null && !firstImage.isEmpty()) {
	                existingProduct.setFirstImage(firstImage.getBytes());
	            }
	            if (secondImage != null && !secondImage.isEmpty()) {
	                existingProduct.setSecondImage(secondImage.getBytes());
	            }
	            if (thirdImage != null && !thirdImage.isEmpty()) {
	                existingProduct.setThirdImage(thirdImage.getBytes());
	            }
	            if (fourthImage != null && !fourthImage.isEmpty()) {
	                existingProduct.setFourthImage(fourthImage.getBytes());
	            }
	            if (fifthImage != null && !fifthImage.isEmpty()) {
	                existingProduct.setFifthImage(fifthImage.getBytes());
	            }

	            // Save the updated product
	            productService.saveProduct(existingProduct);
	            redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update product.");
	        }

	        return "redirect:/retailShop/showProducts";
	    }
}
