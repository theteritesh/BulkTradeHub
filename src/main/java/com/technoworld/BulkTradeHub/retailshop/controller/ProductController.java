package com.technoworld.BulkTradeHub.retailshop.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;
import com.technoworld.BulkTradeHub.retailshop.entity.Product;
import com.technoworld.BulkTradeHub.retailshop.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

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
    		@RequestParam("image") MultipartFile file,
    		RedirectAttributes redirectAttributes) {
    	
    	try {
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
	    	product.setImage(file.getBytes());
	    	
	    	 // Save the product
	        productService.saveProduct(product);
	        redirectAttributes.addFlashAttribute("successMessage", "Product added successfully!");
	    	
		} catch (IOException e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMessage", "Failed to add product.");
		}
    	
    	return "redirect:/dashboard/addProduct";
    }
     
    
    @GetMapping("/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getProductImage(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product != null && product.getImage() != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) 
                    .body(product.getImage());
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
                                @RequestParam(value = "image", required = false) MultipartFile file,
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

            // Update image only if a new file is uploaded
            if (file != null && !file.isEmpty()) {
                existingProduct.setImage(file.getBytes());
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
    public String searchProducts(@RequestParam("query") String query, Model model) {
        List<Product> filteredProducts = productService.searchProducts(query);
        model.addAttribute("products", filteredProducts);
        model.addAttribute("query", query); // Keep the search term in input field
        return "/retailshop/showProduct"; // Ensure the correct Thymeleaf template is used
    }

}
