package com.raaflahar.hcs_idn.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.raaflahar.hcs_idn.dto.response.LoginResponse;
import com.raaflahar.hcs_idn.entity.User;
import com.raaflahar.hcs_idn.repository.UserRepository;
import com.raaflahar.hcs_idn.service.RefreshTokenService;
import com.raaflahar.hcs_idn.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse createNewAccessToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        DecodedJWT decodedJWT = com.auth0.jwt.JWT.decode(refreshToken);
        String username = decodedJWT.getSubject();

        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        String newAccessToken = jwtUtil.generateAccessToken(user.getUsername());

        List<String> roles = user.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.toList());

        return LoginResponse.builder()
                .token(newAccessToken)
                .refreshToken(refreshToken)
                .username(user.getUsername())
                .roles(roles)
                .build();
    }
}
