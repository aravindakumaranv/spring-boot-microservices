package com.shopping_microservices.order_service.sevice;

import java.util.UUID;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.shopping_microservices.order_service.dto.OrderLineItemsDto;
import com.shopping_microservices.order_service.dto.OrderRequest;
import com.shopping_microservices.order_service.model.Order;
import com.shopping_microservices.order_service.model.OrderLineItems;
import com.shopping_microservices.order_service.repository.OrderRepository;

import io.netty.resolver.DefaultAddressResolverGroup;
import lombok.RequiredArgsConstructor;
import reactor.netty.http.client.HttpClient;

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
        HttpClient httpClient = HttpClient.create().resolver(DefaultAddressResolverGroup.INSTANCE);
        boolean result = webClientBuilder.clientConnector(new ReactorClientHttpConnector(httpClient)).build().get()
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
