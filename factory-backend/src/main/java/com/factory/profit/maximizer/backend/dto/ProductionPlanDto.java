package com.factory.profit.maximizer.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductionPlanDto(
        List<OptimizedProductDto> productionPlan,
        BigDecimal totalProfit
) {
}