package com.raaflahar.hcs_idn.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raaflahar.hcs_idn.dto.request.LoginRequest;
import com.raaflahar.hcs_idn.dto.response.CommonResponse;
import com.raaflahar.hcs_idn.dto.response.LoginResponse;
import com.raaflahar.hcs_idn.security.CustomUserDetails;
import com.raaflahar.hcs_idn.service.RefreshTokenService;
import com.raaflahar.hcs_idn.util.JwtUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication Management APIs")
public class AuthController {

        private final AuthenticationManager authenticationManager;
        private final JwtUtil jwtUtil;
        private final RefreshTokenService refreshTokenService;

        @PostMapping("/login")
        public ResponseEntity<CommonResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername());
                String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

                List<String> roles = userDetails.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList());

                LoginResponse loginResponse = LoginResponse.builder()
                                .token(accessToken)
                                .refreshToken(refreshToken)
                                .username(userDetails.getUsername())
                                .roles(roles)
                                .build();

                CommonResponse<LoginResponse> response = CommonResponse.success("Login successful", loginResponse);
                return ResponseEntity.ok(response);
        }

        @PostMapping("/refresh")
        public ResponseEntity<CommonResponse<LoginResponse>> refreshToken(@RequestBody String refreshToken) {
                LoginResponse loginResponse = refreshTokenService.createNewAccessToken(refreshToken);
                CommonResponse<LoginResponse> response = CommonResponse.success("Token refreshed successfully",
                                loginResponse);
                return ResponseEntity.ok(response);
        }
}
