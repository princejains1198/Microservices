package com.eazybytes.loans.service;


import com.eazybytes.loans.dto.LoansDto;

public interface LoansService {

    /**
     * Creates a new loan record for the given mobile number.
     *
     * @param mobileNumber the mobile number associated with the loan
     */
    void createLoan(String mobileNumber);

    /**
     * Retrieves loan details for the given mobile number.
     *
     * @param mobileNumber the mobile number associated with the loan
     * @return LoansDto containing loan details
     LoansDto fetchLoansDetails(String mobileNumber);
    **/
    LoansDto fetchLoansDetails(String mobileNumber);

    /**
     * Updates the loan details.
     *
     * @param loansDto the LoansDto containing updated loan details
     * @return true if the update was successful, false otherwise
     */
    boolean updateLoan(LoansDto loansDto);

    /**
     * Deletes the loan record associated with the given mobile number.
     *
     * @param mobileNumber the mobile number associated with the loan
     * @return true if the deletion was successful, false otherwise
     */
    boolean deleteLoan(String mobileNumber);

}
