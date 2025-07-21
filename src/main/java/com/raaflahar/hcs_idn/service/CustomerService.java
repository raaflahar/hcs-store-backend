package com.raaflahar.hcs_idn.service;

import com.raaflahar.hcs_idn.dto.request.CustomerRequest;
import com.raaflahar.hcs_idn.dto.response.CustomerResponse;
import org.springframework.data.domain.Page;

public interface CustomerService {
    CustomerResponse create(CustomerRequest request);
    CustomerResponse getById(String id);
    Page<CustomerResponse> getAll(Integer page, Integer size);
    CustomerResponse update(CustomerRequest request);
    void deleteById(String id);
}
