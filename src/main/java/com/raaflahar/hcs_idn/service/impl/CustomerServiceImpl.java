package com.raaflahar.hcs_idn.service.impl;

import com.raaflahar.hcs_idn.dto.request.CustomerRequest;
import com.raaflahar.hcs_idn.dto.response.CustomerResponse;
import com.raaflahar.hcs_idn.entity.Customer;
import com.raaflahar.hcs_idn.repository.CustomerRepository;
import com.raaflahar.hcs_idn.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CustomerResponse create(CustomerRequest request) {
        String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();

        Customer customer = Customer.builder()
                .name(request.getName())
                .birthDate(request.getBirthDate())
                .birthPlace(request.getBirthPlace())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdBy(loggedInUser)
                .createdAt(new Date())
                .build();
        customerRepository.save(customer);
        return toCustomerResponse(customer);
    }

    @Override
    public CustomerResponse getById(String id) {
        Customer customer = customerRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
        return toCustomerResponse(customer);
    }

    @Override
    public Page<CustomerResponse> getAll(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = customerRepository.findAll(pageable);
        return customers.map(this::toCustomerResponse);
    }

    @Override
    public CustomerResponse update(CustomerRequest request) {
        String loggedInUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findById(UUID.fromString(request.getEmail()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        customer.setName(request.getName());
        customer.setBirthDate(request.getBirthDate());
        customer.setBirthPlace(request.getBirthPlace());
        customer.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            customer.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        customer.setUpdatedBy(loggedInUser);
        customer.setUpdatedAt(new Date());
        customerRepository.save(customer);
        return toCustomerResponse(customer);
    }

    @Override
    public void deleteById(String id) {
        Customer customer = customerRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
        customerRepository.delete(customer);
    }

    private CustomerResponse toCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .birthDate(customer.getBirthDate())
                .birthPlace(customer.getBirthPlace())
                .email(customer.getEmail())
                .createdBy(customer.getCreatedBy())
                .createdAt(customer.getCreatedAt())
                .updatedBy(customer.getUpdatedBy())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}
