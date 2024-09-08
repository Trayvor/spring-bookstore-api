package org.example.bookstore.repository.order.item;

import java.util.List;
import java.util.Optional;
import org.example.bookstore.model.OrderItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @EntityGraph(attributePaths = {"book"})
    Optional<OrderItem> findByIdAndOrderIdAndOrderUserId(Long orderItemId, Long orderId,
                                                         Long userId);

    @EntityGraph(attributePaths = {"book"})
    List<OrderItem> findAllByOrderIdAndOrderUserId(Long orderId, Long userId);
}
