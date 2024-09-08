package org.example.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.order.ChangeOrderStatusRequestDto;
import org.example.bookstore.dto.order.CreateOrderRequestDto;
import org.example.bookstore.dto.order.OrderDto;
import org.example.bookstore.dto.order.item.OrderItemDto;
import org.example.bookstore.model.User;
import org.example.bookstore.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management",
        description = "Endpoint for order management")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "View order history", description = "View user`s order history")
    public List<OrderDto> getOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAllByUserId(user.getId());
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Create order", description = "Creates order from user`s shopping cart")
    public OrderDto createOrder(@RequestBody @Valid CreateOrderRequestDto createOrderRequestDto,
                                Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.createOrder(createOrderRequestDto, user.getId());
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMAIN')")
    @Operation(summary = "Change order status", description = "Change status of user`s order by "
            + "admin")
    public OrderDto changeOrderStatus(@PathVariable Long id,
                                       @RequestBody @Valid ChangeOrderStatusRequestDto requestDto) {
        return orderService.changeOrderStatus(requestDto, id);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "View specific order by id", description = "View specific user`s order "
            + "with id")
    public List<OrderItemDto> getOrderItemsById(@PathVariable Long orderId,
                                             Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return orderService.getItemsById(orderId, user.getId());
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "View secific item of specific order by id", description = "View "
            + "specific user`s item in order by ids")
    public OrderItemDto getOrderItemById(@PathVariable Long orderId, @PathVariable Long itemId,
                                         Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return orderService.getItemById(orderId, itemId, user.getId());
    }

    private User getAuthenticatedUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
