package com.raaflahar.hcs_idn.service.impl;

import com.raaflahar.hcs_idn.dto.request.ProductRequest;
import com.raaflahar.hcs_idn.dto.response.ProductResponse;
import com.raaflahar.hcs_idn.dto.response.TaxResponse;
import com.raaflahar.hcs_idn.entity.Product;
import com.raaflahar.hcs_idn.entity.Tax;
import com.raaflahar.hcs_idn.repository.ProductRepository;
import com.raaflahar.hcs_idn.repository.TaxRepository;
import com.raaflahar.hcs_idn.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final TaxRepository taxRepository;

    @Override
    public ProductResponse create(ProductRequest request) {
        List<Tax> taxes = taxRepository.findAllById(request.getTaxIds());
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .taxes(taxes)
                .build();
        productRepository.save(product);
        return toProductResponse(product);
    }

    @Override
    public ProductResponse getById(String id) {
        Product product = productRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return toProductResponse(product);
    }

    @Override
    public Page<ProductResponse> getAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(this::toProductResponse);
    }

    @Override
    public ProductResponse update(ProductRequest request) {
        Product product = productRepository.findById(UUID.fromString(request.getName()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        List<Tax> taxes = taxRepository.findAllById(request.getTaxIds());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setTaxes(taxes);
        productRepository.save(product);
        return toProductResponse(product);
    }

    @Override
    public void deleteById(String id) {
        Product product = productRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        productRepository.delete(product);
    }

    private ProductResponse toProductResponse(Product product) {
        List<TaxResponse> taxResponses = product.getTaxes().stream()
                .map(tax -> TaxResponse.builder()
                        .id(tax.getId())
                        .name(tax.getName())
                        .rate(tax.getRate())
                        .build())
                .collect(Collectors.toList());

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .taxes(taxResponses)
                .build();
    }
}
