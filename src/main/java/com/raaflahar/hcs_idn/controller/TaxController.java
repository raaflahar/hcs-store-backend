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

import com.raaflahar.hcs_idn.dto.request.TaxRequest;
import com.raaflahar.hcs_idn.dto.response.CommonResponse;
import com.raaflahar.hcs_idn.dto.response.TaxResponse;
import com.raaflahar.hcs_idn.service.TaxService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/taxes")
@RequiredArgsConstructor
@Tag(name = "Tax", description = "Tax Management APIs")
public class TaxController {

    private final TaxService taxService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<TaxResponse>> createTax(@RequestBody TaxRequest request) {
        TaxResponse taxResponse = taxService.create(request);
        CommonResponse<TaxResponse> response = CommonResponse.created("Successfully created tax", taxResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<TaxResponse>> getTaxById(@PathVariable String id) {
        TaxResponse taxResponse = taxService.getById(id);
        CommonResponse<TaxResponse> response = CommonResponse.success("Successfully get tax by id", taxResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<Page<TaxResponse>>> getAllTaxes(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<TaxResponse> taxResponses = taxService.getAll(page, size);
        CommonResponse<Page<TaxResponse>> response = CommonResponse.success("Successfully get all taxes", taxResponses);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<TaxResponse>> updateTax(@RequestBody TaxRequest request) {
        TaxResponse taxResponse = taxService.update(request);
        CommonResponse<TaxResponse> response = CommonResponse.success("Successfully updated tax", taxResponse);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<String>> deleteTax(@PathVariable String id) {
        taxService.deleteById(id);
        CommonResponse<String> response = CommonResponse.success("Successfully deleted tax", null);
        return ResponseEntity.ok(response);
    }
}
