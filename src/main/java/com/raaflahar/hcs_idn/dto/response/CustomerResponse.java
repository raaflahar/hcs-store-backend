package com.raaflahar.hcs_idn.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private UUID id;
    private String name;
    private Date birthDate;
    private String birthPlace;
    private String email;
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updatedAt;
}
