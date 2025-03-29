package com.technoworld.BulkTradeHub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.technoworld.BulkTradeHub.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	User findByEmail(String email);
	
	@Query("SELECT u FROM User u LEFT JOIN FETCH u.profile")
	List<User> findAllWithProfile();



}
