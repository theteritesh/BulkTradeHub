package com.technoworld.BulkTradeHub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technoworld.BulkTradeHub.entity.DeliveryAddresses;


@Repository
public interface DeliveryAddressesRepository extends JpaRepository<DeliveryAddresses, Integer>{

	List<DeliveryAddresses> findAllByUserId(int id);

	Optional<DeliveryAddresses> findByUserIdAndIsPrimaryTrue(int id);

}
