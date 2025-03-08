package com.technoworld.BulkTradeHub.retailshop.service;


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
}
