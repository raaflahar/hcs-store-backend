package com.raaflahar.hcs_idn.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDetailResponse {
    private UUID id;
    private UUID productId;
    private Integer quantity;
    private Long priceAtPurchase;
}
