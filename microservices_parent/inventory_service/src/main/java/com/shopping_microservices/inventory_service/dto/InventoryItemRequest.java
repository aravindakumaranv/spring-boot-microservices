package com.shopping_microservices.inventory_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryItemRequest {
    
    private String skuCode;
    private Integer quantity;
}
