package com.technoworld.BulkTradeHub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.technoworld.BulkTradeHub.entity.Brand;
import com.technoworld.BulkTradeHub.entity.Category;
import com.technoworld.BulkTradeHub.entity.User;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

	List<Brand> findByUserAndStatusNot(int user, String status);
	
	boolean existsByNameAndUser(String name, int userId);
	
	List<Brand> findByNameContainingAndUser(String name, int userId);
	
	List<Brand> findByUserAndStatusIn(int user, List<String> statuses);
	
	List<Brand> findByStatusIn(List<String> statuses);
	
	@Query(value = "SELECT * FROM bulktradehub.brand WHERE user = ?1", nativeQuery = true)
	List<Brand> getUserById(int id);

}

