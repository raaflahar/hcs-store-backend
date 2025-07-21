package com.raaflahar.hcs_idn.service;

import com.raaflahar.hcs_idn.dto.request.TransactionRequest;
import com.raaflahar.hcs_idn.dto.request.TransactionSearchDTO;
import com.raaflahar.hcs_idn.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TransactionService {
    TransactionResponse create(TransactionRequest request);
    TransactionResponse getById(String id);
    Page<TransactionResponse> getAll(Integer page, Integer size, TransactionSearchDTO searchDTO);
    void deleteById(String id);
    Double getTotalAmountSpentByCustomerBetweenDates(String customerId, Date startDate, Date endDate);
    Double getTotalAmountSpentByCustomer(String customerId);
    Map<String, Double> getTotalAmountSpentPerTax();
    Map<String, Double> getTotalAmountSpentPerProduct();
    byte[] generateCsvReport(String customerId, Date startDate, Date endDate);
}
