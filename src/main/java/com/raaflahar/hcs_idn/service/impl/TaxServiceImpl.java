package com.raaflahar.hcs_idn.service.impl;

import com.raaflahar.hcs_idn.dto.request.TaxRequest;
import com.raaflahar.hcs_idn.dto.response.TaxResponse;
import com.raaflahar.hcs_idn.entity.Tax;
import com.raaflahar.hcs_idn.repository.TaxRepository;
import com.raaflahar.hcs_idn.service.TaxService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TaxServiceImpl implements TaxService {

    private final TaxRepository taxRepository;

    @Override
    public TaxResponse create(TaxRequest request) {
        Tax tax = Tax.builder()
                .name(request.getName())
                .rate(request.getRate())
                .build();
        taxRepository.save(tax);
        return toTaxResponse(tax);
    }

    @Override
    public TaxResponse getById(String id) {
        Tax tax = taxRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tax not found"));
        return toTaxResponse(tax);
    }

    @Override
    public Page<TaxResponse> getAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Tax> taxes = taxRepository.findAll(pageable);
        return taxes.map(this::toTaxResponse);
    }

    @Override
    public TaxResponse update(TaxRequest request) {
        Tax tax = taxRepository.findById(UUID.fromString(request.getName()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tax not found"));
        tax.setName(request.getName());
        tax.setRate(request.getRate());
        taxRepository.save(tax);
        return toTaxResponse(tax);
    }

    @Override
    public void deleteById(String id) {
        Tax tax = taxRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tax not found"));
        taxRepository.delete(tax);
    }

    private TaxResponse toTaxResponse(Tax tax) {
        return TaxResponse.builder()
                .id(tax.getId())
                .name(tax.getName())
                .rate(tax.getRate())
                .build();
    }
}
