package com.raaflahar.hcs_idn.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.raaflahar.hcs_idn.dto.request.TransactionRequest;
import com.raaflahar.hcs_idn.dto.request.TransactionSearchDTO;
import com.raaflahar.hcs_idn.dto.response.CommonResponse;
import com.raaflahar.hcs_idn.dto.response.TransactionResponse;
import com.raaflahar.hcs_idn.service.TransactionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "Transaction Management APIs")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CommonResponse<TransactionResponse>> createTransaction(@RequestBody TransactionRequest request) {
        TransactionResponse transactionResponse = transactionService.create(request);
        CommonResponse<TransactionResponse> response = CommonResponse.created("Successfully created transaction", transactionResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CommonResponse<TransactionResponse>> getTransactionById(@PathVariable String id) {
        TransactionResponse transactionResponse = transactionService.getById(id);
        CommonResponse<TransactionResponse> response = CommonResponse.success("Successfully get transaction by id", transactionResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CommonResponse<Page<TransactionResponse>>> getAllTransactions(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) List<String> paymentStatuses,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction,
            @RequestParam(required = false) String staffName) {

        TransactionSearchDTO searchDTO = TransactionSearchDTO.builder()
                .startDate(startDate)
                .endDate(endDate)
                .customerName(customerName)
                .paymentStatuses(paymentStatuses != null ? paymentStatuses.stream().map(s -> com.raaflahar.hcs_idn.constant.PaymentStatus.valueOf(s.toUpperCase())).collect(java.util.stream.Collectors.toList()) : null)
                .paymentMethod(paymentMethod)
                .sortBy(sortBy)
                .direction(direction)
                .staffName(staffName)
                .build();

        Page<TransactionResponse> transactionResponses = transactionService.getAll(page, size, searchDTO);
        CommonResponse<Page<TransactionResponse>> response = CommonResponse.success("Successfully get all transactions", transactionResponses);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<String>> deleteTransaction(@PathVariable String id) {
        transactionService.deleteById(id);
        CommonResponse<String> response = CommonResponse.success("Successfully deleted transaction", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reports/customer/{customerId}/spent-between-dates")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CommonResponse<Double>> getTotalAmountSpentByCustomerBetweenDates(
            @PathVariable String customerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        Double totalAmount = transactionService.getTotalAmountSpentByCustomerBetweenDates(customerId, startDate, endDate);
        CommonResponse<Double> response = CommonResponse.success("Successfully get total amount spent by customer between dates", totalAmount);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reports/customer/{customerId}/total-spent")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CommonResponse<Double>> getTotalAmountSpentByCustomer(@PathVariable String customerId) {
        Double totalAmount = transactionService.getTotalAmountSpentByCustomer(customerId);
        CommonResponse<Double> response = CommonResponse.success("Successfully get total amount spent by customer", totalAmount);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reports/spent-per-tax")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CommonResponse<Map<String, Double>>> getTotalAmountSpentPerTax() {
        Map<String, Double> amountPerTax = transactionService.getTotalAmountSpentPerTax();
        CommonResponse<Map<String, Double>> response = CommonResponse.success("Successfully get total amount spent per tax", amountPerTax);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reports/spent-per-product")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CommonResponse<Map<String, Double>>> getTotalAmountSpentPerProduct() {
        Map<String, Double> amountPerProduct = transactionService.getTotalAmountSpentPerProduct();
        CommonResponse<Map<String, Double>> response = CommonResponse.success("Successfully get total amount spent per product", amountPerProduct);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/reports/download")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<byte[]> downloadTransactionReport(
            @RequestParam String customerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        byte[] reportData = transactionService.generateCsvReport(customerId, startDate, endDate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "transaction-report.csv");
        headers.setContentLength(reportData.length);

        return new ResponseEntity<>(reportData, headers, HttpStatus.OK);
    }
}
