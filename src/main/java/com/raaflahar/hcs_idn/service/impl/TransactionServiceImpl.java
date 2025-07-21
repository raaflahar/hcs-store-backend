package com.raaflahar.hcs_idn.service.impl;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.raaflahar.hcs_idn.dto.request.TransactionSearchDTO;
import com.raaflahar.hcs_idn.specification.TransactionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.opencsv.CSVWriter;
import com.raaflahar.hcs_idn.dto.request.TransactionRequest;
import com.raaflahar.hcs_idn.dto.response.TransactionDetailResponse;
import com.raaflahar.hcs_idn.dto.response.TransactionResponse;
import com.raaflahar.hcs_idn.entity.Customer;
import com.raaflahar.hcs_idn.entity.Product;
import com.raaflahar.hcs_idn.entity.Tax;
import com.raaflahar.hcs_idn.entity.Transaction;
import com.raaflahar.hcs_idn.entity.TransactionDetail;
import com.raaflahar.hcs_idn.entity.User;
import com.raaflahar.hcs_idn.repository.CustomerRepository;
import com.raaflahar.hcs_idn.repository.ProductRepository;
import com.raaflahar.hcs_idn.repository.TransactionRepository;
import com.raaflahar.hcs_idn.repository.UserRepository;
import com.raaflahar.hcs_idn.service.TransactionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TransactionServiceImpl implements TransactionService {

        private final TransactionRepository transactionRepository;
        private final CustomerRepository customerRepository;
        private final ProductRepository productRepository;
        private final UserRepository userRepository;

        @Override
        public TransactionResponse create(TransactionRequest request) {
                String loggedInUserUsername = SecurityContextHolder.getContext().getAuthentication().getName();
                User user = userRepository.findByUsernameOrEmail(loggedInUserUsername, loggedInUserUsername)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                                "User not found"));

                Customer customer = customerRepository.findById(request.getCustomerId())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Customer not found"));

                Transaction transaction = Transaction.builder()
                                .customer(customer)
                                .createdBy(user)
                                .transactionTime(new Date())
                                .paymentMethod(request.getPaymentMethod())
                                .paymentStatus(request.getPaymentStatus())
                                .build();

                List<TransactionDetail> transactionDetails = request.getTransactionDetails().stream()
                                .map(detailRequest -> {
                                        Product product = productRepository.findById(detailRequest.getProductId())
                                                        .orElseThrow(() -> new ResponseStatusException(
                                                                        HttpStatus.NOT_FOUND, "Product not found"));

                                        return TransactionDetail.builder()
                                                        .transaction(transaction)
                                                        .product(product)
                                                        .quantity(detailRequest.getQuantity())
                                                        .priceAtPurchase(product.getPrice())
                                                        .build();
                                }).collect(Collectors.toList());

                transaction.setTransactionDetails(transactionDetails);
                long netAmountPaid = 0;
                long totalTaxPaid = 0;

                for (TransactionDetail detail : transactionDetails) {
                        long productPrice = detail.getPriceAtPurchase() * detail.getQuantity();
                        netAmountPaid += productPrice;

                        for (Tax tax : detail.getProduct().getTaxes()) {
                                totalTaxPaid += (long) (productPrice * tax.getRate() / 100);
                        }
                }

                transaction.setNetAmountPaid(netAmountPaid);
                transaction.setTotalTaxPaid(totalTaxPaid);
                transaction.setTotalAmountPaid(netAmountPaid + totalTaxPaid);

                transactionRepository.save(transaction);

                return toTransactionResponse(transaction);
        }

        @Override
        public TransactionResponse getById(String id) {
                Transaction transaction = transactionRepository.findById(UUID.fromString(id))
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Transaction not found"));
                return toTransactionResponse(transaction);
        }

        @Override
        public Page<TransactionResponse> getAll(Integer page, Integer size, TransactionSearchDTO searchDTO) {
                Pageable pageable = PageRequest.of(page, size);
                Page<Transaction> transactions = transactionRepository.findAll(TransactionSpecification.getSpecification(searchDTO), pageable);
                return transactions.map(this::toTransactionResponse);
        }

        @Override
        public void deleteById(String id) {
                Transaction transaction = transactionRepository.findById(UUID.fromString(id))
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                                "Transaction not found"));
                transactionRepository.delete(transaction);
        }

        @Override
        public Double getTotalAmountSpentByCustomerBetweenDates(String customerId, Date startDate, Date endDate) {
                List<Transaction> transactions = transactionRepository.findAllByCustomerIdAndTransactionTimeBetween(
                                UUID.fromString(customerId), startDate, endDate);
                return transactions.stream().mapToDouble(Transaction::getTotalAmountPaid).sum();
        }

        @Override
        public Double getTotalAmountSpentByCustomer(String customerId) {
                List<Transaction> transactions = transactionRepository.findAllByCustomerId(UUID.fromString(customerId));
                return transactions.stream().mapToDouble(Transaction::getTotalAmountPaid).sum();
        }

        @Override
        public Map<String, Double> getTotalAmountSpentPerTax() {
                Map<String, Double> amountPerTax = new HashMap<>();
                List<Transaction> transactions = transactionRepository.findAll();

                for (Transaction transaction : transactions) {
                        for (TransactionDetail detail : transaction.getTransactionDetails()) {
                                double productPrice = detail.getPriceAtPurchase() * detail.getQuantity();
                                for (Tax tax : detail.getProduct().getTaxes()) {
                                        double taxAmount = productPrice * tax.getRate() / 100;
                                        amountPerTax.merge(tax.getName(), taxAmount, Double::sum);
                                }
                        }
                }
                return amountPerTax;
        }

        @Override
        public Map<String, Double> getTotalAmountSpentPerProduct() {
                Map<String, Double> amountPerProduct = new HashMap<>();
                List<Transaction> transactions = transactionRepository.findAll();

                for (Transaction transaction : transactions) {
                        for (TransactionDetail detail : transaction.getTransactionDetails()) {
                                double productPrice = detail.getPriceAtPurchase() * detail.getQuantity();
                                amountPerProduct.merge(detail.getProduct().getName(), productPrice, Double::sum);
                        }
                }
                return amountPerProduct;
        }

        @Override
        public byte[] generateCsvReport(String customerId, Date startDate, Date endDate) {
                List<Transaction> transactions = transactionRepository.findAllByCustomerIdAndTransactionTimeBetween(
                                UUID.fromString(customerId), startDate, endDate);

                try (StringWriter writer = new StringWriter();
                                CSVWriter csvWriter = new CSVWriter(writer)) {

                        String[] header = { "Transaction ID", "Transaction Time", "Net Amount Paid",
                                        "Total Amount Paid", "Total Tax Paid", "Payment Method", "Payment Status" };
                        csvWriter.writeNext(header);

                        for (Transaction transaction : transactions) {
                                String[] data = {
                                                transaction.getId().toString(),
                                                transaction.getTransactionTime().toString(),
                                                String.valueOf(transaction.getNetAmountPaid()),
                                                String.valueOf(transaction.getTotalAmountPaid()),
                                                String.valueOf(transaction.getTotalTaxPaid()),
                                                transaction.getPaymentMethod(),
                                                transaction.getPaymentStatus().name()
                                };
                                csvWriter.writeNext(data);
                        }
                        csvWriter.flush();
                        return writer.toString().getBytes(StandardCharsets.UTF_8);
                } catch (Exception e) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Failed to generate CSV report");
                }
        }

        private TransactionResponse toTransactionResponse(Transaction transaction) {
                List<TransactionDetailResponse> detailResponses = transaction.getTransactionDetails().stream()
                                .map(detail -> TransactionDetailResponse.builder()
                                                .id(detail.getId())
                                                .productId(detail.getProduct().getId())
                                                .quantity(detail.getQuantity())
                                                .priceAtPurchase(detail.getPriceAtPurchase())
                                                .build())
                                .collect(Collectors.toList());

                return TransactionResponse.builder()
                                .id(transaction.getId())
                                .customerId(transaction.getCustomer().getId())
                                .createdBy(transaction.getCreatedBy().getFullName())
                                .netAmountPaid(transaction.getNetAmountPaid())
                                .totalAmountPaid(transaction.getTotalAmountPaid())
                                .totalTaxPaid(transaction.getTotalTaxPaid())
                                .transactionTime(transaction.getTransactionTime())
                                .paymentMethod(transaction.getPaymentMethod())
                                .paymentStatus(transaction.getPaymentStatus())
                                .transactionDetails(detailResponses)
                                .build();
        }
}
