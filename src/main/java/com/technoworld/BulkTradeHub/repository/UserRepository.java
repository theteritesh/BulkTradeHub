package com.technoworld.BulkTradeHub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.technoworld.BulkTradeHub.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	User findByEmail(String email);
	
    @Query("SELECT u FROM User u WHERE u.roles = 'ROLE_RETAIL'")
    List<User> findAllRetailUsers();
    

    @Query("SELECT u FROM User u WHERE u.roles = 'ROLE_SALESMAN'")
    List<User> findAllSalesamneUsers();
}
