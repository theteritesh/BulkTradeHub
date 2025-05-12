package com.technoworld.BulkTradeHub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.technoworld.BulkTradeHub.entity.Cart;
import com.technoworld.BulkTradeHub.entity.ProductPost;
import com.technoworld.BulkTradeHub.entity.User;

public interface CartRepository extends JpaRepository<Cart, Integer> {

	Optional<Cart> findByUserAndProductPost(User user, ProductPost productPost);

	List<Cart> findByUserAndStatusOrderByAddedAtDesc(User user, Boolean status);


}
