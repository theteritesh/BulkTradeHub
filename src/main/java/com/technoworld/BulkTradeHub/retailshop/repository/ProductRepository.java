package com.technoworld.BulkTradeHub.retailshop.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.retailshop.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	@Query("SELECT p FROM Product p WHERE p.user.id = :userId AND (" +
		       "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
		       "LOWER(p.category) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
		       "LOWER(p.brand) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
		       "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')))" +
		       " ORDER BY p.id DESC")
		List<Product> searchProductsByUser(@Param("query") String query, @Param("userId") int userId);
	
	List<Product> findByUserOrderByIdDesc(User user);

	
		
}

