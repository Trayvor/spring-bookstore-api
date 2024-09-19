package org.example.bookstore.repository.shopping.cart;

import java.util.Optional;
import org.example.bookstore.model.ShoppingCart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @EntityGraph("cartItems")
    Optional<ShoppingCart> findByUserId(@Param("id") Long id);
}
