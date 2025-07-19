package com.technoworld.BulkTradeHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technoworld.BulkTradeHub.entity.RazorpayCredentials;

@Repository
public interface RazorpayCredentialsRepository extends JpaRepository<RazorpayCredentials, Integer> {
	
}

