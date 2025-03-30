package com.technoworld.BulkTradeHub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.technoworld.BulkTradeHub.entity.Product;
import com.technoworld.BulkTradeHub.entity.Profile;
import com.technoworld.BulkTradeHub.entity.RetailShopProfile;
import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.repository.ProductRepository;
import com.technoworld.BulkTradeHub.repository.ProfileRepository;
import com.technoworld.BulkTradeHub.repository.RetailShopProfileRepository;
import com.technoworld.BulkTradeHub.repository.UserRepository;

@Service
public class AdminServiceImplementation {
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private RetailShopProfileRepository retailShopProfileRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProfileRepository profileRepository;


	public List<User> getAllUser() {
		List<User> lst = userRepository.findAll();
		return lst;
	}

	public List<RetailShopProfile> getAllRetailShopProfileData() {
		List<RetailShopProfile> lst = retailShopProfileRepository.findAll();
		return lst;
	}

	public List<Product> getAllRetailShopProduct() {
		List<Product> lst = productRepository.findAll();
		return lst;
	}

}
