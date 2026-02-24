package com.factory.profit.maximizer.backend.repository;

import com.factory.profit.maximizer.backend.model.ProductComposition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCompositionRepository extends JpaRepository<ProductComposition, Long> {
}
