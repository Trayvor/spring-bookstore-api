package org.example.bookstore.service;

import java.util.List;
import org.example.bookstore.dto.order.ChangeOrderStatusRequestDto;
import org.example.bookstore.dto.order.CreateOrderRequestDto;
import org.example.bookstore.dto.order.OrderDto;
import org.example.bookstore.dto.order.item.OrderItemDto;

public interface OrderService {
    List<OrderDto> findAllByUserId(Long userId);

    OrderDto createOrder(CreateOrderRequestDto createOrderRequestDto, Long userId);

    OrderDto changeOrderStatus(ChangeOrderStatusRequestDto requestDto, Long orderId);

    List<OrderItemDto> getItemsById(Long orderId, Long userId);

    OrderItemDto getItemById(Long orderId, Long itemId, Long userId);
}
