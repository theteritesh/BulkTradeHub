package com.technoworld.BulkTradeHub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technoworld.BulkTradeHub.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	List<Category> findByUser(int id);
	boolean existsByNameAndUser(String name, int userId);
	List<Category> findByNameContainingAndUser(String name, int userId);


}

