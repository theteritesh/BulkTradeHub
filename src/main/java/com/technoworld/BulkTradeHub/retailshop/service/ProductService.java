package com.technoworld.BulkTradeHub.retailshop.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.technoworld.BulkTradeHub.entity.User;
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
    
    public List<Product> getAllProducts(User user) {
        return productRepository.findByUserOrderByIdDesc(user);
    }
    
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }
    
    public List<Product> searchProducts(String query,int userId) {
        return productRepository.searchProductsByUser(query,userId);
    }
}
