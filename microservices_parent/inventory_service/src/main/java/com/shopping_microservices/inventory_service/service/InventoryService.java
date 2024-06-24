package com.shopping_microservices.inventory_service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopping_microservices.inventory_service.dto.InventoryItemRequest;
import com.shopping_microservices.inventory_service.model.InventoryItem;
import com.shopping_microservices.inventory_service.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {
    
    private final InventoryRepository inventoryRepository;

    public void createInventoryItem(InventoryItemRequest inventoryItemRequest) {
        InventoryItem inventoryItem = InventoryItem.builder()
            .skuCode(inventoryItemRequest.getSkuCode())
            .quantity(inventoryItemRequest.getQuantity())
            .build();
        inventoryRepository.save(inventoryItem);
    }

    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode, Integer quantity) {
        if(inventoryRepository.findBySkuCode().isPresent()) {
            if(inventoryRepository.findBySkuCode().get().getQuantity()>=quantity){
                return true;
            }
        }
        return false;
    }
}
