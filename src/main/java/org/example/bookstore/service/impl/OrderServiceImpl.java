package org.example.bookstore.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.order.ChangeOrderStatusRequestDto;
import org.example.bookstore.dto.order.CreateOrderRequestDto;
import org.example.bookstore.dto.order.OrderDto;
import org.example.bookstore.dto.order.item.OrderItemDto;
import org.example.bookstore.exception.DataProcessingException;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.ItemCartOrderMapper;
import org.example.bookstore.mapper.OrderItemMapper;
import org.example.bookstore.mapper.OrderMapper;
import org.example.bookstore.model.CartItem;
import org.example.bookstore.model.Order;
import org.example.bookstore.model.OrderItem;
import org.example.bookstore.model.ShoppingCart;
import org.example.bookstore.model.User;
import org.example.bookstore.model.constant.Status;
import org.example.bookstore.repository.order.OrderRepository;
import org.example.bookstore.repository.order.item.OrderItemRepository;
import org.example.bookstore.repository.shopping.cart.ShoppingCartRepository;
import org.example.bookstore.service.OrderService;
import org.example.bookstore.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartService shoppingCartService;
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final ItemCartOrderMapper itemCartOrderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public List<OrderDto> findAllByUserId(Long userId) {
        return orderMapper.toDto(orderRepository.findAllByUserId(userId));
    }

    @Override
    public OrderDto createOrder(CreateOrderRequestDto createOrderRequestDto, Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find shopping cart with user id " + userId)
        );
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new DataProcessingException("Can`t create order, because shopping cart of user "
                    + "with id " + userId + " is empty");
        }
        Order order = createBlankOrder(createOrderRequestDto, userId);

        for (CartItem cartItem : shoppingCart.getCartItems()) {
            OrderItem orderItem = itemCartOrderMapper.toOrderItem(cartItem);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
            order.setTotal(order.getTotal()
                    .add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))));
        }
        shoppingCartService.deleteAllFromShoppingCart(userId);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto changeOrderStatus(ChangeOrderStatusRequestDto requestDto, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find order with id " + orderId)
        );
        order.setStatus(requestDto.status());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemDto> getItemsById(Long orderId, Long userId) {
        List<OrderItem> items =
                orderItemRepository.findAllByOrderIdAndOrderUserId(orderId, userId);
        return orderItemMapper.toDto(items);
    }

    @Override
    public OrderItemDto getItemById(Long orderId, Long itemId, Long userId) {
        OrderItem orderItem = orderItemRepository
                .findByIdAndOrderIdAndOrderUserId(itemId, orderId, userId).orElseThrow(
                        () -> new EntityNotFoundException("Can`t find order item with id "
                                + itemId
                                + "that depends to order  with id " + orderId
                                + " that depends to user with"
                                + " id " + userId)
                );
        return orderItemMapper.toDto(orderItem);
    }

    private Order createBlankOrder(CreateOrderRequestDto createOrderRequestDto, Long userId) {
        Order order = new Order();
        order.setUser(new User(userId));
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Status.PENDING);
        order.setShippingAddress(createOrderRequestDto.shippingAddress());

        order.setTotal(new BigDecimal(0));
        return orderRepository.save(order);
    }
}
