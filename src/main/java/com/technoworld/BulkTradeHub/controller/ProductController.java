package com.technoworld.BulkTradeHub.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.technoworld.BulkTradeHub.entity.Product;
import com.technoworld.BulkTradeHub.entity.ProductGtinInfo;
import com.technoworld.BulkTradeHub.entity.ProductPost;
import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.repository.ProductGtinInfoRepository;
import com.technoworld.BulkTradeHub.repository.ProductPostRepository;
import com.technoworld.BulkTradeHub.repository.ProductRepository;
import com.technoworld.BulkTradeHub.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private static final String API_KEY = "uw3z06j249lqnhx3heay8x7gmb9p67";
    private static final String API_URL = "https://api.barcodelookup.com/v3/products";
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductPostRepository productPostRepository;
    
    @Autowired
    private ProductGtinInfoRepository productRepo;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @PostMapping("/add")
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
	    	
	        productService.saveProduct(product);
	        redirectAttributes.addFlashAttribute("successMessage", "Product added successfully!");
	    	
		} catch (IOException e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMessage", "Failed to add product.");
		}
    	
    	return "redirect:/dashboard/addProduct";
    }
     
    
    @GetMapping("/{imageType}/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id, @PathVariable String imageType) {
        Product product = productService.getProductById(id);
        
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

    			
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProductById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete product.");
        }
        return "redirect:/dashboard/showProducts";
    }
    
    
    @PostMapping("/update")
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
                return "redirect:/dashboard/showProducts";
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

        return "redirect:/dashboard/showProducts";
    }
    
    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query, Model model,Principal principal) {
    	User user =  (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        List<Product> filteredProducts = productService.searchProducts(query,user.getId());
        model.addAttribute("products", filteredProducts);
        model.addAttribute("query", query);
        return "/retailshop/showProduct"; // Ensure the correct Thymeleaf template is used
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
        Optional<ProductGtinInfo> localProduct = productRepo.findByGtin(gtin);
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

    
    @GetMapping("/searchProduct")
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

        return "redirect:/products/searchProduct?productId=" + productId;
    }



}
