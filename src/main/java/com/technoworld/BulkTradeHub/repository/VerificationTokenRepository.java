package com.technoworld.BulkTradeHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.technoworld.BulkTradeHub.entity.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}
