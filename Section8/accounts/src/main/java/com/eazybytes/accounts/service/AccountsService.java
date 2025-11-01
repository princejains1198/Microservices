package com.eazybytes.accounts.service;

import com.eazybytes.accounts.dto.CustomerDto;

public interface AccountsService {

    /**
     * Create a new account for the given customer.
     * @param customerDto - CustomerDto object containing customer details
     */
    void createAccount(CustomerDto customerDto);

    /**
     * Fetch account details for the given mobile number.
     * @param mobileNumber - Mobile number of the customer
     * @return CustomerDto containing customer and account details
     */
    CustomerDto fetchAccount(String mobileNumber);

    /**
     * Update account details for the given customer.
     * @param customerDto - CustomerDto object containing updated customer details
     * @return boolean indicating success or failure of the update operation
     */
    boolean updateAccount(CustomerDto customerDto);

    /**
     * Delete account for the given mobile number.
     * @param mobileNumber - Mobile number of the customer
     * @return boolean indicating success or failure of the delete operation
     */
    boolean deleteAccount(String mobileNumber);

}
