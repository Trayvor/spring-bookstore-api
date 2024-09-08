package org.example.bookstore.mapper;

import java.util.List;
import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.order.OrderDto;
import org.example.bookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    OrderDto toDto(Order order);

    List<OrderDto> toDto(List<Order> orders);
}
