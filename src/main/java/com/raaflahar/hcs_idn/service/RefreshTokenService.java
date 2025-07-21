package com.raaflahar.hcs_idn.service;

import com.raaflahar.hcs_idn.dto.response.LoginResponse;

public interface RefreshTokenService {
    LoginResponse createNewAccessToken(String refreshToken);
}
