package com.raaflahar.hcs_idn.repository;

import com.raaflahar.hcs_idn.constant.ERole;
import com.raaflahar.hcs_idn.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(ERole name);
}