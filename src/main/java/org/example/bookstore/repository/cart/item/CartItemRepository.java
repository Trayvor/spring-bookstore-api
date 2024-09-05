package org.example.bookstore.repository.cart.item;

import java.util.Optional;
import org.example.bookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteCartItemsByBook_Id(Long bookId);

    Optional<CartItem> findByIdAndShoppingCartUserId(Long itemId, Long userId);

    boolean existsByIdAndShoppingCartUserId(Long itemId, Long userId);
}
