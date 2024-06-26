package com.shopping_microservices.order_service.sevice;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.shopping_microservices.order_service.dto.OrderLineItemsDto;
import com.shopping_microservices.order_service.dto.OrderRequest;
import com.shopping_microservices.order_service.model.Order;
import com.shopping_microservices.order_service.model.OrderLineItems;
import com.shopping_microservices.order_service.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        java.util.List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
            .stream()
            .map(this::mapToDto)
            .toList();

        order.setOrderLineItems(orderLineItems);
        boolean result = webClientBuilder.build().get()
            .uri("http://inventory-service/api/inventory/{sku-code}/{quantity}",1,2)
            .retrieve()
            .bodyToMono(Boolean.class)
            .block();
        orderRepository.save(order);
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }

}
