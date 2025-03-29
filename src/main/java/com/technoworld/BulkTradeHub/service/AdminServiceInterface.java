package com.technoworld.BulkTradeHub.service;

import java.util.List;
import java.util.Optional;

import com.technoworld.BulkTradeHub.entity.Profile;
import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.retailshop.entity.Product;
import com.technoworld.BulkTradeHub.retailshop.entity.RetailShopProfile;

public interface AdminServiceInterface {
public List<User> getAllUser(); 
public List<RetailShopProfile> getAllRetailShopProfileData();
public List<Product> getAllRetailShopProduct();
}
