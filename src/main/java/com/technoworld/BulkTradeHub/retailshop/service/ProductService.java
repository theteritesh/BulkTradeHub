package com.technoworld.BulkTradeHub.retailshop.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.technoworld.BulkTradeHub.retailshop.entity.Product;
import com.technoworld.BulkTradeHub.retailshop.repository.ProductRepository;
import java.util.List;

@Service
public class ProductService {
	
	@Autowired
    private  ProductRepository productRepository;

    public ProductService() {
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    
}
