package com.raaflahar.hcs_idn.repository;

import com.raaflahar.hcs_idn.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {
    List<Transaction> findAllByCustomerId(UUID customerId);
    List<Transaction> findAllByCustomerIdAndTransactionTimeBetween(UUID customerId, Date startDate, Date endDate);
}
