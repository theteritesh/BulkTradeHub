package com.technoworld.BulkTradeHub.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.technoworld.BulkTradeHub.entity.ProductPost;
import com.technoworld.BulkTradeHub.entity.User;

@Repository
public interface ProductPostRepository extends JpaRepository<ProductPost, Integer> {

	Page<ProductPost> findAllByOrderByPostedAtDesc(Pageable pageable);

	List<ProductPost> findAllByUserOrderByIdDesc(User user);
	
	@Query(value = "SELECT * FROM bulktradehub.product_post " +
            "WHERE user_id = :id " +
            "AND (LOWER(product_name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(description) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(brand) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "ORDER BY id DESC", 
    nativeQuery = true)
	List<ProductPost> searchProductPost(@Param("query") String query, @Param("id") int id);
	
	@Query("SELECT COUNT(p) FROM ProductPost p WHERE p.user = :user")
	long countTotalProductsByUser(User user);

}
