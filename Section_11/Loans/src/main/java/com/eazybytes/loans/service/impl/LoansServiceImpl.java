package com.eazybytes.loans.service.impl;

import com.eazybytes.loans.Constants.LoansConstants;
import com.eazybytes.loans.dto.LoansDto;
import com.eazybytes.loans.entity.Loans;
import com.eazybytes.loans.exception.LoansAlreadyExistsException;
import com.eazybytes.loans.exception.ResourceNotFoundException;
import com.eazybytes.loans.mapper.LoansMapper;
import com.eazybytes.loans.repository.LoansRepository;
import com.eazybytes.loans.service.LoansService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LoansServiceImpl implements LoansService {

    private LoansRepository loansRepository;

    /**
     * Creates a new loan record for the given mobile number.
     *
     * @param mobileNumber the mobile number associated with the loan
     */
    @Override
    public void createLoan(String mobileNumber) {
        Optional<Loans> optionalLoans = loansRepository.findByMobileNumber(mobileNumber);
        if (optionalLoans.isPresent()) {
            throw new LoansAlreadyExistsException("Loan already registered with given mobile number: " + mobileNumber);
        }
        loansRepository.save(createNewLoan(mobileNumber));
    }

    private Loans createNewLoan(String mobileNumber) {
        Loans loans = new Loans();

        // Ensure a 12-digit loan number: range 100_000_000_000 .. 999_999_999_999
        Long randomLoanNumber = 100000000000L + (long) (Math.random() * 900000000000L);
        loans.setLoanNumber(Long.toString(randomLoanNumber));
        loans.setMobileNumber(mobileNumber);
        loans.setLoanType(LoansConstants.HOME_LOAN);
        loans.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        loans.setAmountPaid(0);
        loans.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        return loans;
    }

    /**
     * Retrieves loan details for the given mobile number.
     *
     * @param mobileNumber the mobile number associated with the loan
     * @return LoansDto containing loan details
     * LoansDto fetchLoansDetails(String mobileNumber);
     **/
    @Override
    public LoansDto fetchLoansDetails(String mobileNumber) {
        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loans Details", "mobileNumber", mobileNumber)
        );
        return LoansMapper.mapToLoansDto(loans, new LoansDto());
    }

    /**
     * Updates the loan details.
     *
     * @param loansDto the LoansDto containing updated loan details
     * @return true if the update was successful, false otherwise
     */
    @Override
    public boolean updateLoan(LoansDto loansDto) {
        boolean isUpdated = false;
        Loans existingLoans = loansRepository.findByLoanNumber(loansDto.getLoanNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Loans Details", "loanNumber", loansDto.getLoanNumber())
        );
        Loans updatedLoans = LoansMapper.mapToLoans(loansDto, existingLoans);
        Loans savedLoans = loansRepository.save(updatedLoans);
        if (savedLoans != null && savedLoans.getLoanNumber().equals(loansDto.getLoanNumber())) {
            isUpdated = true;
        }
        return isUpdated;
    }

    /**
     * Deletes the loan record associated with the given mobile number.
     *
     * @param mobileNumber the mobile number associated with the loan
     * @return true if the deletion was successful, false otherwise
     */
    @Override
    public boolean deleteLoan(String mobileNumber) {
        boolean isUpdated = false;
        Loans existingLoans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loans Details", "mobileNumber", mobileNumber)
        );
        loansRepository.deleteById(existingLoans.getLoanId());
        if (!loansRepository.existsById(existingLoans.getLoanId())) {
            isUpdated = true;
        }
        return isUpdated;
    }


}
