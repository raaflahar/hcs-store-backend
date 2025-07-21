package com.raaflahar.hcs_idn.service;

import com.raaflahar.hcs_idn.dto.request.ProductRequest;
import com.raaflahar.hcs_idn.dto.response.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductResponse create(ProductRequest request);
    ProductResponse getById(String id);
    Page<ProductResponse> getAll(Integer page, Integer size);
    ProductResponse update(ProductRequest request);
    void deleteById(String id);
}
