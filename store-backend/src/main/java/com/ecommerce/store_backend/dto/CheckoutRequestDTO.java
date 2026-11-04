package com.ecommerce.store_backend.dto;

import jakarta.validation.constraints.NotNull;

public class CheckoutRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}