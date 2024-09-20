package org.example.bookstore.repository.order;

import java.util.List;
import java.util.Optional;
import org.example.bookstore.model.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph("orderItems")
    List<Order> findAllByUserId(@Param("id") Long userId);

    @EntityGraph("orderItems")
    Optional<Order> findByIdAndUserId(@Param("orderId") Long orderId, @Param("userId") Long userId);
}
