package org.example.bookstore.dto.order;

import lombok.Data;
import org.example.bookstore.dto.order.item.OrderItemDto;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private List<OrderItemDto> orderItems = new ArrayList<>();
}
