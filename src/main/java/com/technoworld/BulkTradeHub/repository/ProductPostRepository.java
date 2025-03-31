package com.technoworld.BulkTradeHub.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technoworld.BulkTradeHub.entity.ProductPost;

@Repository
public interface ProductPostRepository extends JpaRepository<ProductPost, Integer> {

	Page<ProductPost> findAllByOrderByPostedAtDesc(Pageable pageable);
}
