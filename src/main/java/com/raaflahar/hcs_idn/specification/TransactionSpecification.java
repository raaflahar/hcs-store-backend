package com.raaflahar.hcs_idn.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.raaflahar.hcs_idn.dto.request.TransactionSearchDTO;
import com.raaflahar.hcs_idn.entity.Customer;
import com.raaflahar.hcs_idn.entity.Transaction;
import com.raaflahar.hcs_idn.entity.User;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

public class TransactionSpecification {

    public static Specification<Transaction> getSpecification(TransactionSearchDTO request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getStartDate() != null && request.getEndDate() != null) {
                predicates.add(criteriaBuilder.between(root.get("transactionTime"), request.getStartDate(), request.getEndDate()));
            }

            if (request.getCustomerName() != null && !request.getCustomerName().isEmpty()) {
                Join<Transaction, Customer> customerJoin = root.join("customer", JoinType.INNER);
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(customerJoin.get("name")), "%" + request.getCustomerName().toLowerCase() + "%"));
            }

            if (request.getPaymentStatuses() != null && !request.getPaymentStatuses().isEmpty()) {
                predicates.add(root.get("paymentStatus").in(request.getPaymentStatuses()));
            }

            if (request.getPaymentMethod() != null && !request.getPaymentMethod().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("paymentMethod"), request.getPaymentMethod()));
            }

            if (request.getStaffName() != null && !request.getStaffName().isEmpty()) {
                Join<Transaction, User> userJoin = root.join("user", JoinType.INNER);
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("name")), "%" + request.getStaffName().toLowerCase() + "%"));
            }

            // Sorting
            if (request.getSortBy() != null && !request.getSortBy().isEmpty()) {
                if (request.getDirection() != null && request.getDirection().equalsIgnoreCase("desc")) {
                    query.orderBy(criteriaBuilder.desc(root.get(request.getSortBy())));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(request.getSortBy())));
                }
            } else {
                // Default sorting by transactionTime newest first
                query.orderBy(criteriaBuilder.desc(root.get("transactionTime")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}