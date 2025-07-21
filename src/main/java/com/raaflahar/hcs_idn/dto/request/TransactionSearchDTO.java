package com.raaflahar.hcs_idn.dto.request;

import com.raaflahar.hcs_idn.constant.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionSearchDTO {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String customerName;
    private List<PaymentStatus> paymentStatuses;
    private String paymentMethod;
    private String sortBy;
    private String direction;
    private String staffName;
}