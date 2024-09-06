package org.example.bookstore.repository.order;

import java.util.List;
import java.util.Optional;
import org.example.bookstore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.user.id = :id")
    List<Order> findAllByUserId(@Param("id") Long userId);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.id = :orderId "
            + "AND o.user.id = :userId")
    Optional<Order> findByIdAndUserId(@Param("orderId") Long orderId, @Param("userId") Long userId);
}
