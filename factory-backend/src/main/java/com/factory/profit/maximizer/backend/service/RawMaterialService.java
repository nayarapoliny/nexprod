package com.factory.profit.maximizer.backend.service;

import com.factory.profit.maximizer.backend.dto.RawMaterialDto;
import com.factory.profit.maximizer.backend.exception.ResourceNotFoundException;
import com.factory.profit.maximizer.backend.model.RawMaterial;
import com.factory.profit.maximizer.backend.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;

    @Transactional(readOnly = true)
    public List<RawMaterial> findAll() {
        return rawMaterialRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<RawMaterial> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return rawMaterialRepository.findById(id);
    }

    @Transactional
    public RawMaterial save(RawMaterialDto rawMaterialDto) {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setCode(rawMaterialDto.getCode());
        rawMaterial.setName(rawMaterialDto.getName());
        rawMaterial.setStockQuantity(rawMaterialDto.getStockQuantity());
        return rawMaterialRepository.save(rawMaterial);
    }

    @Transactional
    public RawMaterial update(Long id, RawMaterialDto rawMaterialDto) {
        if (id == null) {
            throw new ResourceNotFoundException("RawMaterial id cannot be null");
        }
        RawMaterial existingRawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RawMaterial not found with id: " + id));

        existingRawMaterial.setCode(rawMaterialDto.getCode());
        existingRawMaterial.setName(rawMaterialDto.getName());
        existingRawMaterial.setStockQuantity(rawMaterialDto.getStockQuantity());

        return rawMaterialRepository.save(existingRawMaterial);
    }

    @Transactional
    public void deleteById(Long id) {
        if (id == null) {
            throw new ResourceNotFoundException("RawMaterial id cannot be null");
        }
        if (!rawMaterialRepository.existsById(id)) {
            throw new ResourceNotFoundException("RawMaterial not found with id: " + id);
        }
        rawMaterialRepository.deleteById(id);
    }
}
