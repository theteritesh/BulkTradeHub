package com.technoworld.BulkTradeHub.retailshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.technoworld.BulkTradeHub.retailshop.entity.Product;
import com.technoworld.BulkTradeHub.retailshop.service.ProductService;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/addProduct")
    public String showAddProductForm(Model model) {
    	Product product=new Product();
        model.addAttribute("product", product.getPrice());
        return "addProduct";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute @Valid Product product,
                             BindingResult result,
                             Model model) {

        if (result.hasErrors()) {
            return "addProduct";
        }
        productService.saveProduct(product);
        return "redirect:/products/list";
    }

    @GetMapping("/list")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "productList";
    }
}
