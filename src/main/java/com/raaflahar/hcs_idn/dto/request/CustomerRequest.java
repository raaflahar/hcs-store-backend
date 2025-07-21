package com.raaflahar.hcs_idn.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {
    private String name;
    private Date birthDate;
    private String birthPlace;
    private String email;
    private String password;
}
