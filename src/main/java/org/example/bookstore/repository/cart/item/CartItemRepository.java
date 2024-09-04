package org.example.bookstore.repository.cart.item;

import java.util.Optional;
import org.example.bookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteCartItemsByBook_Id(Long bookId);

    Optional<CartItem> findByIdAndShoppingCartUserEmail(Long id, String userEmail);

    boolean existsByIdAndShoppingCartUserEmail(Long id, String userEmail);
}
