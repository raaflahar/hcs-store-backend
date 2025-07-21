package com.raaflahar.hcs_idn.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> {
    private Integer statusCode;
    private String message;
    private T data;

    public static <T> CommonResponse<T> success(String message, T data) {
        return CommonResponse.<T>builder()
                .statusCode(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .build();
    }

    public static <T> CommonResponse<T> created(String message, T data) {
        return CommonResponse.<T>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(message)
                .data(data)
                .build();
    }
}
