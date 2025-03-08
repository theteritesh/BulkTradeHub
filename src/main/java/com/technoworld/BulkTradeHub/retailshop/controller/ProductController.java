package com.technoworld.BulkTradeHub.retailshop.controller;

import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
}
