package com.raaflahar.hcs_idn.dto.request;

import java.util.List;
import java.util.UUID;

import com.raaflahar.hcs_idn.constant.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private UUID customerId;
    private String paymentMethod;
    private PaymentStatus paymentStatus;
    private List<TransactionDetailRequest> transactionDetails;
}
