package com.technoworld.BulkTradeHub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.technoworld.BulkTradeHub.entity.RetailShopProfile;
import com.technoworld.BulkTradeHub.repository.RetailShopProfileRepository;

@Service
public class RetailShopProfileService {
	@Autowired
    private RetailShopProfileRepository retailShopProfileRepository;

    public RetailShopProfile saveProfile(RetailShopProfile profile) {
        return retailShopProfileRepository.save(profile);
    }

    public RetailShopProfile getProfile(int id) {
        return retailShopProfileRepository.findById(id).orElse(new RetailShopProfile());
    }

}
