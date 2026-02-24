package com.factory.profit.maximizer.backend.service;

import com.factory.profit.maximizer.backend.dto.ProductCompositionDto;
import com.factory.profit.maximizer.backend.dto.ProductDto;
import com.factory.profit.maximizer.backend.exception.ResourceNotFoundException;
import com.factory.profit.maximizer.backend.model.Product;
import com.factory.profit.maximizer.backend.model.ProductComposition;
import com.factory.profit.maximizer.backend.model.RawMaterial;
import com.factory.profit.maximizer.backend.repository.ProductCompositionRepository;
import com.factory.profit.maximizer.backend.repository.ProductRepository;
import com.factory.profit.maximizer.backend.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final ProductCompositionRepository productCompositionRepository;

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return productRepository.findById(id);
    }

    @Transactional
    public Product save(ProductDto productDto) {
        Product product = new Product();
        product.setCode(productDto.getCode());
        product.setName(productDto.getName());
        product.setValue(productDto.getValue());

        Set<ProductComposition> composition = buildComposition(product, productDto.getComposition());
        product.setComposition(composition);

        return productRepository.save(product);
    }

    @Transactional
    public Product update(Long id, ProductDto productDto) {
        if (id == null) {
            throw new ResourceNotFoundException("Product id cannot be null");
        }
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        existingProduct.setCode(productDto.getCode());
        existingProduct.setName(productDto.getName());
        existingProduct.setValue(productDto.getValue());

        // Clear old composition and build the new one
        Set<ProductComposition> oldComposition = existingProduct.getComposition();
        if (oldComposition != null && !oldComposition.isEmpty()) {
            productCompositionRepository.deleteAll(oldComposition);
        }
        Set<ProductComposition> newComposition = buildComposition(existingProduct, productDto.getComposition());
        existingProduct.setComposition(newComposition);

        return productRepository.save(existingProduct);
    }

    @Transactional
    public void deleteById(Long id) {
        if (id == null || !productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    private Set<ProductComposition> buildComposition(Product product, Set<ProductCompositionDto> compositionDtos) {
        Set<ProductComposition> composition = new HashSet<>();
        if (compositionDtos != null) {
            for (ProductCompositionDto compDto : compositionDtos) {
                Long rawMaterialId = compDto.getRawMaterialId();
                if (rawMaterialId == null) {
                    throw new ResourceNotFoundException("RawMaterial id cannot be null");
                }
                RawMaterial rawMaterial = rawMaterialRepository.findById(rawMaterialId)
                        .orElseThrow(() -> new ResourceNotFoundException("RawMaterial not found with id: " + rawMaterialId));
                composition.add(new ProductComposition(null, product, rawMaterial, compDto.getQuantity()));
            }
        }
        return composition;
    }
}
