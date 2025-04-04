package com.technoworld.BulkTradeHub.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.technoworld.BulkTradeHub.entity.Product;
import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.repository.ProductRepository;

@ControllerAdvice
public class DashboardLayoutAttribute {

    @Autowired
    private ProductRepository productRepository;

    @ModelAttribute("lowStockProducts")
    public List<Product> addLowStockProductsToModel(Principal principal) {
        if (principal == null) {
            return Collections.emptyList(); 
        }

        User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return productRepository.findLowStockProductsByUser(user);
    }

}
