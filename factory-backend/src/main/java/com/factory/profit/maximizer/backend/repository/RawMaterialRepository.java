package com.factory.profit.maximizer.backend.repository;

import com.factory.profit.maximizer.backend.model.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long> {
}