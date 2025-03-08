package com.technoworld.BulkTradeHub.retailshop.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.technoworld.BulkTradeHub.retailshop.entity.Product;
import com.technoworld.BulkTradeHub.retailshop.repository.ProductRepository;

@Service
public class ProductService {
	
	
    private final ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
		this.productRepository=productRepository;
	}
    
    public void saveProduct(Product product) {
        productRepository.save(product);
    }   
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }
    
    public List<Product> searchProducts(String query) {
        return productRepository.searchProducts(query);
    }
}
