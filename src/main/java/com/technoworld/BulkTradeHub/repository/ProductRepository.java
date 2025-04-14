package com.technoworld.BulkTradeHub.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.technoworld.BulkTradeHub.entity.Product;
import com.technoworld.BulkTradeHub.entity.ProductPost;
import com.technoworld.BulkTradeHub.entity.User;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	
	@Query("SELECT p FROM Product p WHERE p.user.id = :userId AND (" +
		       "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
		       "LOWER(p.category) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
		       "LOWER(p.brand) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
		       "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')))" +
		       " ORDER BY p.id DESC")
		List<Product> searchProductsByUser(@Param("query") String query, @Param("userId") int userId);
	
	@Query("SELECT p FROM Product p WHERE p.user = :user AND p.totalQuantity > 10 ORDER BY p.id DESC")
	List<Product> findByUserOrderByIdDesc(@Param("user") User user);
	
	@Query("SELECT p FROM Product p WHERE p.id = :id AND p.user.id = :userId")
    Optional<Product> findByIdAndUser(Long id, int userId);

	Product findFirstByUserOrderByIdDesc(User user);

	@Query("SELECT p FROM Product p WHERE p.totalQuantity <= 10 AND p.user = :user ORDER BY p.totalQuantity ASC")
	List<Product> findLowStockProductsByUser(@Param("user") User user);
	
	@Query("SELECT p FROM Product p WHERE p.user.id = :userId AND p.totalQuantity <= 10 AND (" +
		       "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
		       "LOWER(p.category) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
		       "LOWER(p.brand) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
		       "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')))" +
		       " ORDER BY p.totalQuantity ASC")
	List<Product> searchLowStockProductsByUser(@Param("query") String query, @Param("userId") int userId);
	
	long countByUser(User user);
	
	@Query("SELECT COUNT(p) FROM Product p WHERE p.user = :user AND p.totalQuantity = 0")
    long countOutOfStockProductsByUser(User user);
	
	@Query("SELECT COUNT(p) FROM Product p WHERE p.user = :user AND p.totalQuantity BETWEEN 1 AND 10")
	long countLowStockProductsByUser(User user);

	
		
}

