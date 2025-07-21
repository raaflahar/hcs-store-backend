package com.raaflahar.hcs_idn.controller;

import com.raaflahar.hcs_idn.dto.request.UserRequest;
import com.raaflahar.hcs_idn.dto.response.CommonResponse;
import com.raaflahar.hcs_idn.dto.response.UserResponse;
import com.raaflahar.hcs_idn.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User Management APIs")
public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<UserResponse>> createUser(@RequestBody UserRequest request) {
        UserResponse userResponse = userService.createUser(request);
        CommonResponse<UserResponse> response = CommonResponse.created("Successfully created user", userResponse);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CommonResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> userResponses = userService.getAllUser();
        CommonResponse<List<UserResponse>> response = CommonResponse.success("Successfully retrieved all users", userResponses);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<UserResponse>> updateUser(@PathVariable UUID id, @RequestBody UserRequest request) {
        UserResponse userResponse = userService.updateUser(id, request);
        CommonResponse<UserResponse> response = CommonResponse.success("Successfully updated user", userResponse);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponse<String>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        CommonResponse<String> response = CommonResponse.success("Successfully deleted user", null);
        return ResponseEntity.ok(response);
    }
}
