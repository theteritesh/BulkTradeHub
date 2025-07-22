package com.technoworld.BulkTradeHub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.technoworld.BulkTradeHub.entity.OrderItems;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, Integer> {

	@Query(value = "Select * from order_items where order_id =:orderId", nativeQuery = true)
	List<OrderItems> findOrderItemByOderId(@Param("orderId") int orderId);
	
}

