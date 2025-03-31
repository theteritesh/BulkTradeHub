package com.technoworld.BulkTradeHub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technoworld.BulkTradeHub.entity.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

	List<Brand> findByUser(int id);
	boolean existsByNameAndUser(String name, int userId);
	List<Brand> findByNameContainingAndUser(String name, int userId);

}

