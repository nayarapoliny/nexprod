package com.factory.profit.maximizer.backend.repository;

import com.factory.profit.maximizer.backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}