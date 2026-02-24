package com.factory.profit.maximizer.backend.service;

import com.factory.profit.maximizer.backend.dto.OptimizedProductDto;
import com.factory.profit.maximizer.backend.dto.ProductionPlanDto;
import com.factory.profit.maximizer.backend.model.Product;
import com.factory.profit.maximizer.backend.model.ProductComposition;
import com.factory.profit.maximizer.backend.model.RawMaterial;
import com.factory.profit.maximizer.backend.repository.ProductRepository;
import com.factory.profit.maximizer.backend.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductionOptimizerService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    @Transactional(readOnly = true)
    public ProductionPlanDto calculateOptimalProductionPlan() {
        // 1. Fetch all necessary data from the database
        List<Product> products = productRepository.findAll();
        Map<Long, Integer> availableStock = rawMaterialRepository.findAll().stream()
                .collect(Collectors.toMap(RawMaterial::getId, RawMaterial::getStockQuantity));

        // 2. Sort products by a calculated efficiency score (value per resource cost)
        products.sort(Comparator.comparing(this::getEfficiencyScore).reversed());

        List<OptimizedProductDto> productionPlan = new ArrayList<>();
        BigDecimal totalProfit = BigDecimal.ZERO;

        // 3. Iterate through sorted products to decide how many to produce
        for (Product product : products) {
            if (product.getComposition().isEmpty()) {
                continue; // Cannot produce a product with no composition defined
            }

            // 4. Calculate the maximum number of units we can produce for this product
            int maxUnitsToProduce = calculateMaxProducibleUnits(product, availableStock);

            if (maxUnitsToProduce > 0) {
                // 5. "Produce" the items: add to plan and update stock
                productionPlan.add(new OptimizedProductDto(
                        product.getCode(),
                        product.getName(),
                        maxUnitsToProduce,
                        product.getValue(),
                        product.getValue().multiply(BigDecimal.valueOf(maxUnitsToProduce))
                ));

                // Update total profit
                totalProfit = totalProfit.add(product.getValue().multiply(BigDecimal.valueOf(maxUnitsToProduce)));

                // Update the available stock of raw materials
                for (ProductComposition component : product.getComposition()) {
                    Long rawMaterialId = component.getRawMaterial().getId();
                    int quantityNeeded = component.getQuantity();
                    availableStock.computeIfPresent(rawMaterialId,
                            (id, currentStock) -> currentStock - (maxUnitsToProduce * quantityNeeded)
                    );
                }
            }
        }

        return new ProductionPlanDto(productionPlan, totalProfit);
    }

    /**
     * Calculates an efficiency score for a product to help prioritize production.
     * The score is defined as value / total_quantity_of_materials.
     * A higher score means more value per unit of resource.
     */
    private double getEfficiencyScore(Product product) {
        if (product.getComposition() == null || product.getComposition().isEmpty()) {
            return 0;
        }

        double totalResourceCost = product.getComposition().stream()
                .mapToDouble(ProductComposition::getQuantity)
                .sum();

        if (totalResourceCost == 0) {
            return 0; // Avoid division by zero; a product that costs nothing is infinitely efficient
        }

        return product.getValue().doubleValue() / totalResourceCost;
    }

    /**
     * Determines the maximum number of units of a single product that can be
     * produced based on the currently available stock of raw materials.
     */
    private int calculateMaxProducibleUnits(Product product, Map<Long, Integer> availableStock) {
        int maxPossibleUnits = Integer.MAX_VALUE;

        for (ProductComposition component : product.getComposition()) {
            RawMaterial material = component.getRawMaterial();
            int quantityNeededPerUnit = component.getQuantity();

            if (quantityNeededPerUnit <= 0) {
                continue; // Skip if material quantity is zero or negative
            }

            int stock = availableStock.getOrDefault(material.getId(), 0);
            int unitsPossibleForThisMaterial = stock / quantityNeededPerUnit;

            if (unitsPossibleForThisMaterial < maxPossibleUnits) {
                maxPossibleUnits = unitsPossibleForThisMaterial;
            }
        }

        return maxPossibleUnits == Integer.MAX_VALUE ? 0 : maxPossibleUnits;
    }
}