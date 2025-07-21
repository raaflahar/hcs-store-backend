package com.raaflahar.hcs_idn.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.raaflahar.hcs_idn.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, UUID>{
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByUsername(String username);
}
