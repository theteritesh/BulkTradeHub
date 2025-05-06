package com.technoworld.BulkTradeHub.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.technoworld.BulkTradeHub.entity.Category;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	List<Category> findByUserAndStatusNot(int user, String status);
	
	boolean existsByNameAndUser(String name, int userId);
	
	List<Category> findByNameContainingAndUser(String name, int userId);
	
	List<Category> findByStatusIn(List<String> statuses);
	
	List<Category> findByUserAndStatusIn(int user, List<String> statuses);
	
	@Query(value = "SELECT * FROM bulktradehub.category WHERE user = ?1", nativeQuery = true)
	List<Category> getUserById(int id);
}

