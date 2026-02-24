package com.factory.profit.maximizer.backend.controller;

import com.factory.profit.maximizer.backend.dto.ProductionPlanDto;
import com.factory.profit.maximizer.backend.service.ProductionOptimizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/production")
@RequiredArgsConstructor
public class ProductionController {

    private final ProductionOptimizerService optimizerService;

    @GetMapping("/optimize")
    public ResponseEntity<ProductionPlanDto> getOptimalProductionPlan() {
        return ResponseEntity.ok(optimizerService.calculateOptimalProductionPlan());
    }
}