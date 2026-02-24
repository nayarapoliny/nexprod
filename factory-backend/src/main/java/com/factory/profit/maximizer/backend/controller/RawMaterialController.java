package com.factory.profit.maximizer.backend.controller;

import com.factory.profit.maximizer.backend.dto.RawMaterialDto;
import com.factory.profit.maximizer.backend.model.RawMaterial;
import com.factory.profit.maximizer.backend.service.RawMaterialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/raw-materials")
@RequiredArgsConstructor
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    @GetMapping
    public ResponseEntity<List<RawMaterial>> getAllRawMaterials() {
        List<RawMaterial> rawMaterials = rawMaterialService.findAll();
        return ResponseEntity.ok(rawMaterials);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RawMaterial> getRawMaterialById(@PathVariable Long id) {
        return rawMaterialService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RawMaterial> createRawMaterial(@Valid @RequestBody RawMaterialDto rawMaterialDto) {
        RawMaterial savedRawMaterial = rawMaterialService.save(rawMaterialDto);
        return new ResponseEntity<>(savedRawMaterial, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RawMaterial> updateRawMaterial(@PathVariable Long id, @Valid @RequestBody RawMaterialDto rawMaterialDto) {
        RawMaterial updatedRawMaterial = rawMaterialService.update(id, rawMaterialDto);
        return ResponseEntity.ok(updatedRawMaterial);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRawMaterial(@PathVariable Long id) {
        rawMaterialService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
