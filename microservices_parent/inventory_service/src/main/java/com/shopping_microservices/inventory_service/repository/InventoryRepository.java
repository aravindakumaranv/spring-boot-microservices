package com.shopping_microservices.inventory_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping_microservices.inventory_service.model.InventoryItem;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long>{

    Optional<InventoryItem> findBySkuCode();

}
