package com.raaflahar.hcs_idn.service;

import com.raaflahar.hcs_idn.dto.request.TaxRequest;
import com.raaflahar.hcs_idn.dto.response.TaxResponse;
import org.springframework.data.domain.Page;

public interface TaxService {
    TaxResponse create(TaxRequest request);
    TaxResponse getById(String id);
    Page<TaxResponse> getAll(Integer page, Integer size);
    TaxResponse update(TaxRequest request);
    void deleteById(String id);
}
