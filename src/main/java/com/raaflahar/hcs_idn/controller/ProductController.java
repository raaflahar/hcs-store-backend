package com.raaflahar.hcs_idn.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.raaflahar.hcs_idn.dto.request.ProductRequest;
import com.raaflahar.hcs_idn.dto.response.CommonResponse;
import com.raaflahar.hcs_idn.dto.response.ProductResponse;
import com.raaflahar.hcs_idn.service.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Product Management APIs")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<ProductResponse>> createProduct(@RequestBody ProductRequest request) {
        ProductResponse productResponse = productService.create(request);
        CommonResponse<ProductResponse> response = CommonResponse.created("Successfully created product", productResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<ProductResponse>> getProductById(@PathVariable String id) {
        ProductResponse productResponse = productService.getById(id);
        CommonResponse<ProductResponse> response = CommonResponse.success("Successfully get product by id", productResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<Page<ProductResponse>>> getAllProducts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<ProductResponse> productResponses = productService.getAll(page, size);
        CommonResponse<Page<ProductResponse>> response = CommonResponse.success("Successfully get all products", productResponses);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<ProductResponse>> updateProduct(@RequestBody ProductRequest request) {
        ProductResponse productResponse = productService.update(request);
        CommonResponse<ProductResponse> response = CommonResponse.success("Successfully updated product", productResponse);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<String>> deleteProduct(@PathVariable String id) {
        productService.deleteById(id);
        CommonResponse<String> response = CommonResponse.success("Successfully deleted product", null);
        return ResponseEntity.ok(response);
    }
}
