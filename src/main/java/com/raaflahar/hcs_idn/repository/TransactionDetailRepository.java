package com.raaflahar.hcs_idn.repository;

import com.raaflahar.hcs_idn.entity.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, UUID> {
}
