package com.raaflahar.hcs_idn.dto.response;

import java.util.Date;
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
public class TransactionResponse {
    private UUID id;
    private UUID customerId;
    private String createdBy;
    private Long netAmountPaid;
    private Long totalAmountPaid;
    private Long totalTaxPaid;
    private Date transactionTime;
    private String paymentMethod;
    private PaymentStatus paymentStatus;
    private List<TransactionDetailResponse> transactionDetails;
}
