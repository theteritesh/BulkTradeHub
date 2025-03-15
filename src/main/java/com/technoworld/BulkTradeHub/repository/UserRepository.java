package com.technoworld.BulkTradeHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.technoworld.BulkTradeHub.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	User readByEmail(String email);

}
