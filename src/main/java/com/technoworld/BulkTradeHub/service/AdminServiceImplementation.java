package com.technoworld.BulkTradeHub.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.technoworld.BulkTradeHub.entity.Profile;
import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.repository.ProfileRepository;
import com.technoworld.BulkTradeHub.repository.UserRepository;
import com.technoworld.BulkTradeHub.retailshop.entity.Product;
import com.technoworld.BulkTradeHub.retailshop.entity.RetailShopProfile;
import com.technoworld.BulkTradeHub.retailshop.repository.ProductRepository;
import com.technoworld.BulkTradeHub.retailshop.repository.RetailShopProfileRepository;

@Service
public class AdminServiceImplementation implements AdminServiceInterface {
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private RetailShopProfileRepository retailShopProfileRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProfileRepository profileRepository;

	@Override
	public List<User> getAllUser() {
		List<User> lst = userRepository.findAllWithProfile();
		return lst;
	}

	@Override
	public List<RetailShopProfile> getAllRetailShopProfileData() {
		List<RetailShopProfile> lst = retailShopProfileRepository.findAll();
		return lst;
	}

	@Override
	public List<Product> getAllRetailShopProduct() {
		List<Product> lst = productRepository.findAll();
		return lst;
	}

}
