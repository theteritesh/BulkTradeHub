package com.technoworld.BulkTradeHub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technoworld.BulkTradeHub.entity.OrderItems;
import com.technoworld.BulkTradeHub.entity.UserOrders;

@Repository
public interface UserOrderRepository extends JpaRepository<UserOrders, Integer> {
	
	Optional<UserOrders> findByIdAndBuyerId(int id, int buyerId);
}

