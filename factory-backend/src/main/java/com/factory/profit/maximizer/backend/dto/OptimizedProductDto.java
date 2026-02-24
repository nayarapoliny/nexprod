package com.factory.profit.maximizer.backend.dto;

import java.math.BigDecimal;

public record OptimizedProductDto(
        String productCode,
        String productName,
        int quantityToProduce,
        BigDecimal unitValue,
        BigDecimal totalValue
) {
}