package org.example.bookstore.repository.shopping.cart;

import java.util.Optional;
import org.example.bookstore.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("SELECT sc FROM ShoppingCart sc LEFT JOIN FETCH"
            + " sc.cartItems WHERE sc.user.email = :email")
    Optional<ShoppingCart> findByUserEmail(@Param("email") String email);
}
