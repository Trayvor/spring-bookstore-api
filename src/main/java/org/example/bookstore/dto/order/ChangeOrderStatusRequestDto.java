package org.example.bookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import org.example.bookstore.model.constant.Status;

public record ChangeOrderStatusRequestDto(@NotNull Status status) {
}
