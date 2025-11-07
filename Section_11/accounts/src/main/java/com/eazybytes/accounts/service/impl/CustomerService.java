package com.eazybytes.accounts.service.impl;

import com.eazybytes.accounts.dto.CustomerDetailsDto;

public interface CustomerService {

    /**
     * Fetches customer details based on the provided mobile number.
     *
     * @param mobileNumber the mobile number of the customer
     * @return CustomerDetailsDto containing customer information
     */
    CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId);

}
