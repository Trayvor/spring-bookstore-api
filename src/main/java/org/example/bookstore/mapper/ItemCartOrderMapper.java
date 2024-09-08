package org.example.bookstore.mapper;

import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.model.CartItem;
import org.example.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ItemCartOrderMapper {
    @Mapping(source = "book.price", target = "price")
    OrderItem toOrderItem(CartItem cartItem);
}
