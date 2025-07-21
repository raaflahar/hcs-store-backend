package com.raaflahar.hcs_idn.service;

import java.util.Date;

public interface ReportService {
    byte[] generateCustomerTransactionReport(String customerId, Date startDate, Date endDate);
}
