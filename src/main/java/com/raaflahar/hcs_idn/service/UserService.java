package com.raaflahar.hcs_idn.service;

import java.util.List;
import java.util.UUID;

import com.raaflahar.hcs_idn.dto.request.UserRequest;
import com.raaflahar.hcs_idn.dto.response.UserResponse;

public interface UserService {
    UserResponse createUser (UserRequest userRequest);
    List<UserResponse> getAllUser();
    UserResponse updateUser(UUID id, UserRequest userRequest);
    void deleteUser(UUID id);
}
