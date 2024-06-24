package com.shopping_microservices.inventory_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.shopping_microservices.inventory_service.dto.InventoryItemRequest;
import com.shopping_microservices.inventory_service.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody InventoryItemRequest inventoryItemRequest) {
        inventoryService.createInventoryItem(inventoryItemRequest);
        return "Added Item to Inventory";
    }

    @GetMapping("/{sku-code}/{quantity}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable("sku-code") String skuCode, @PathVariable("quantity") Integer quantity) {
        return inventoryService.isInStock(skuCode, quantity);
    }

}
