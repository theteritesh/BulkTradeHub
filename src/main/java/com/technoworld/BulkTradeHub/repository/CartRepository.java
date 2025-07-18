package com.technoworld.BulkTradeHub.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.technoworld.BulkTradeHub.entity.Cart;

import jakarta.transaction.Transactional;

public interface CartRepository extends JpaRepository<Cart, Integer> {

	Cart findByUserId(int id);

	Cart findByUserIdAndProductPostId(int id, Integer productPostId);

	@Query(value = """
		    SELECT 
		        c.product_post_id,
		        c.quantity,
		        c.added_at,
		        pp.product_name,
		        pp.wholesale_price,
		        pp.retail_price,
		        pp.min_order_quantity,
		        pp.lots,
		        pp.category,
		        pp.brand,
		        p.main_image,
		        c.id,
		        CASE 
		            WHEN rs.shop_name IS NOT NULL THEN rs.shop_name
		            WHEN bp.business_name IS NOT NULL THEN bp.business_name
		            ELSE 'Unknown'
		        END AS seller_name
		    FROM cart c
		    JOIN product_post pp ON c.product_post_id = pp.id
		    JOIN products p ON pp.product_id = p.id
		    JOIN user u ON pp.user_id = u.id
		    LEFT JOIN profile pr ON u.id = pr.user_id
		    LEFT JOIN retail_shop_profile rs ON pr.id = rs.id
		    LEFT JOIN business_profile bp ON pr.id = bp.id
		    WHERE c.user_id = :userId
		    """, nativeQuery = true)
	List<Object[]> findCartItemsWithDetails(@Param("userId") int userId);
	
	@Transactional
	void deleteAllByUserId(int userId);

	int countByUserId(int id);


}
