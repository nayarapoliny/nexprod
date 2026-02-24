package com.factory.profit.maximizer.backend.service;

import com.factory.profit.maximizer.backend.dto.ProductionPlanDto;
import com.factory.profit.maximizer.backend.model.Product;
import com.factory.profit.maximizer.backend.model.ProductComposition;
import com.factory.profit.maximizer.backend.model.RawMaterial;
import com.factory.profit.maximizer.backend.repository.ProductRepository;
import com.factory.profit.maximizer.backend.repository.RawMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductionOptimizerServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @InjectMocks
    private ProductionOptimizerService productionOptimizerService;

    private RawMaterial wood;
    private RawMaterial steel;
    private Product chair;
    private Product table;

    @BeforeEach
    void setUp() {
        wood = new RawMaterial(1L, "WOOD", "Wood", 100);
        steel = new RawMaterial(2L, "STEEL", "Steel", 50);

        chair = new Product(1L, "CHAIR", "Chair", new BigDecimal("150.00"), new HashSet<>());
        table = new Product(2L, "TABLE", "Table", new BigDecimal("500.00"), new HashSet<>());

        ProductComposition chairCompositionWood = new ProductComposition(1L, chair, wood, 10);
        ProductComposition chairCompositionSteel = new ProductComposition(2L, chair, steel, 5);
        chair.getComposition().add(chairCompositionWood);
        chair.getComposition().add(chairCompositionSteel);

        ProductComposition tableCompositionWood = new ProductComposition(3L, table, wood, 25);
        ProductComposition tableCompositionSteel = new ProductComposition(4L, table, steel, 15);
        table.getComposition().add(tableCompositionWood);
        table.getComposition().add(tableCompositionSteel);
    }

    @Test
    void testCalculateOptimalProductionPlan() {
        when(rawMaterialRepository.findAll()).thenReturn(new ArrayList<>(List.of(wood, steel)));
        when(productRepository.findAll()).thenReturn(new ArrayList<>(List.of(chair, table)));

        ProductionPlanDto productionPlanDto = productionOptimizerService.calculateOptimalProductionPlan();

        assertEquals(2, productionPlanDto.productionPlan().size());
        assertEquals(new BigDecimal("1650.00"), productionPlanDto.totalProfit());

        assertEquals("Table", productionPlanDto.productionPlan().get(0).productName());
        assertEquals(3, productionPlanDto.productionPlan().get(0).quantityToProduce());

        assertEquals("Chair", productionPlanDto.productionPlan().get(1).productName());
        assertEquals(1, productionPlanDto.productionPlan().get(1).quantityToProduce());
    }

    @Test
    void testCalculateOptimalProductionPlanWithNoStock() {
        wood.setStockQuantity(0);
        steel.setStockQuantity(0);

        when(rawMaterialRepository.findAll()).thenReturn(new ArrayList<>(List.of(wood, steel)));
        when(productRepository.findAll()).thenReturn(new ArrayList<>(List.of(chair, table)));

        ProductionPlanDto productionPlanDto = productionOptimizerService.calculateOptimalProductionPlan();

        assertEquals(0, productionPlanDto.productionPlan().size());
        assertEquals(BigDecimal.ZERO, productionPlanDto.totalProfit());
    }

    @Test
    void testCalculateOptimalProductionPlanWithNoProducts() {
        when(rawMaterialRepository.findAll()).thenReturn(List.of(wood, steel));
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        ProductionPlanDto productionPlanDto = productionOptimizerService.calculateOptimalProductionPlan();

        assertEquals(0, productionPlanDto.productionPlan().size());
        assertEquals(BigDecimal.ZERO, productionPlanDto.totalProfit());
    }

    @Test
    void shouldPrioritizeMoreEfficientProductOverHigherValueProduct() {
        // Scenario: 1 high-value, low-efficiency product vs. many low-value, high-efficiency products
        // Raw Materials
        RawMaterial steel = new RawMaterial(1L, "STEEL", "Steel", 100);
        RawMaterial electronics = new RawMaterial(2L, "ELEC", "Electronic Component", 30);
        when(rawMaterialRepository.findAll()).thenReturn(List.of(steel, electronics));

        // Products
        // Low efficiency: 200000 / (100 + 20) = 1666.6
        Product luxuryCar = new Product(1L, "CAR", "Luxury Car", new BigDecimal("200000.00"), new HashSet<>());
        luxuryCar.getComposition().add(new ProductComposition(1L, luxuryCar, steel, 100));
        luxuryCar.getComposition().add(new ProductComposition(2L, luxuryCar, electronics, 20));

        // High efficiency: 15000 / (1 + 1) = 7500
        Product drone = new Product(2L, "DRONE", "Professional Drone", new BigDecimal("15000.00"), new HashSet<>());
        drone.getComposition().add(new ProductComposition(3L, drone, steel, 1));
        drone.getComposition().add(new ProductComposition(4L, drone, electronics, 1));

        when(productRepository.findAll()).thenReturn(new ArrayList<>(List.of(luxuryCar, drone)));

        // --- Execute ---
        ProductionPlanDto result = productionOptimizerService.calculateOptimalProductionPlan();

        // --- Assert ---
        // The old greedy algorithm would produce 1 car for a profit of 200,000.
        // The new efficiency algorithm should produce 30 drones for a profit of 450,000.
        assertEquals(1, result.productionPlan().size(), "Should produce only one type of product.");
        assertEquals("Professional Drone", result.productionPlan().get(0).productName());
        assertEquals(30, result.productionPlan().get(0).quantityToProduce());

        // Use compareTo for BigDecimal comparisons
        assertEquals(0, new BigDecimal("450000.00").compareTo(result.totalProfit()), "Total profit should be from drones.");
    }
}
