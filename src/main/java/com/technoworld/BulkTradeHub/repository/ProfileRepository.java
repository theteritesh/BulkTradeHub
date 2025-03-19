package com.technoworld.BulkTradeHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technoworld.BulkTradeHub.entity.Profile;
import com.technoworld.BulkTradeHub.entity.User;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer>{
	Profile  findByUser(User user);
}
