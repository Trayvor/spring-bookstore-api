package org.example.bookstore.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.example.bookstore.dto.order.item.OrderItemDto;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private List<OrderItemDto> orderItems = new ArrayList<>();
    private LocalDateTime orderDate;
    private BigDecimal total;
    private String status;
}
