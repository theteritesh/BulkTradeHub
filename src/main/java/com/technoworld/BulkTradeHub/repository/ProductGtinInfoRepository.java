package com.technoworld.BulkTradeHub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technoworld.BulkTradeHub.entity.ProductGtinInfo;

@Repository
public interface ProductGtinInfoRepository extends JpaRepository<ProductGtinInfo, Integer> {
    Optional<ProductGtinInfo> findByGtin(String gtin);
}