package com.eazybytes.accounts.service.impl;

import com.eazybytes.accounts.constants.AccountsConstants;
import com.eazybytes.accounts.dto.AccountsDto;
import com.eazybytes.accounts.dto.AccountsMsgDto;
import com.eazybytes.accounts.dto.CustomerDto;
import com.eazybytes.accounts.entity.Accounts;
import com.eazybytes.accounts.entity.Customer;
import com.eazybytes.accounts.exception.CustomerAlreadyExistsException;
import com.eazybytes.accounts.exception.ResourceNotFoundException;
import com.eazybytes.accounts.mapper.AccountMapper;
import com.eazybytes.accounts.mapper.CustomerMapper;
import com.eazybytes.accounts.repository.AccountsRepository;
import com.eazybytes.accounts.repository.CustomerRepository;
import com.eazybytes.accounts.service.AccountsService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@AllArgsConstructor
@Service
public class AccountsServiceImpl implements AccountsService {

    private static final Logger log = LoggerFactory.getLogger(AccountsServiceImpl.class);

    private AccountsRepository accountsRepository;

    private CustomerRepository customerRepository;

    private final StreamBridge streamBridge;

    /**
     * Create a new account for the given customer.
     *
     * @param customerDto - CustomerDto object containing customer details
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());

        Optional<Customer> byMobileNumber = customerRepository.findByMobileNumber(customer.getMobileNumber());
        if (byMobileNumber.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobile number: " + customer.getMobileNumber());
        }

        Customer savedCustomer = customerRepository.save(customer);
        Accounts savedAccount = accountsRepository.save(createNewAccount(savedCustomer));
        sendCommunication(savedAccount, savedCustomer);
    }

//    private void sendCommunication(Accounts accounts, Customer customer) {
//        try {
//            log.info("Sending account creation message to message broker for account number: {}", accounts.getAccountNumber());
//            streamBridge.send("emailSupplier-out-0",
//                    new com.eazybytes.accounts.dto.AccountsMsgDto(
//                            accounts.getAccountNumber(),
//                            customer.getName(),
//                            customer.getEmail(),
//                            customer.getMobileNumber()
//                    ));
//            log.info("Account creation message sent successfully for account number: {}", accounts.getAccountNumber());
//        } catch (Exception e) {
//            log.error("Failed to send account creation message for account number: {}", accounts.getAccountNumber(), e);
//        }
//    }

    private void sendCommunication(Accounts account, Customer customer) {
        var accountsMsgDto = new AccountsMsgDto(account.getAccountNumber(), customer.getName(),
                customer.getEmail(), customer.getMobileNumber());
        log.info("Sending Communication request for the details: {}", accountsMsgDto);
        var result = streamBridge.send("sendCommunication-out-0", accountsMsgDto);
        log.info("Is the Communication request successfully triggered ? : {}", result);
    }

    /**
     * Create a new Accounts entity for the given customer.
     *
     * @param customer
     * @return the newly created Accounts details
     */
    private Accounts createNewAccount(Customer customer) {
        Accounts accounts = new Accounts();
        accounts.setCustomerId(customer.getCustomerId());
        long randomAccountNumber = 1000000000L + new Random().nextInt(900000000);
        accounts.setAccountNumber(randomAccountNumber);
        accounts.setAccountType(AccountsConstants.SAVINGS);
        accounts.setBranchAddress(AccountsConstants.ADDRESS);
        return accounts;
    }

    /**
     * Fetch account details for the given mobile number.
     *
     * @param mobileNumber - Mobile number of the customer
     * @return CustomerDto containing customer and account details
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountMapper.mapToAccountsDto(accounts, new AccountsDto()));

        return customerDto;
        }

    /**
     * Update account details for the given customer.
     *
     * @param customerDto - CustomerDto object containing updated customer details
     * @return boolean indicating success or failure of the update operation
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if (accountsDto != null) {
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "accountNumber", String.valueOf(accountsDto.getAccountNumber()))
            );
            AccountMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "customerId", String.valueOf(customerId))
            );
            CustomerMapper.mapToCustomer(customerDto, customer);
            customer = customerRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    /**
     * Delete account for the given mobile number.
     *
     * @param mobileNumber - Mobile number of the customer
     * @return boolean indicating success or failure of the delete operation
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

    /**
     * Update communication status for the given account number.
     *
     * @param accountNumber - Account number of the customer
     * @return boolean indicating success or failure of the update operation
     */
    @Override
    public boolean updateCommunicationStatus(Long accountNumber) {
        boolean isUpdated = false;
        if(accountNumber != null) {
            Accounts accounts = accountsRepository.findById(accountNumber).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", String.valueOf(accountNumber))
            );
            accounts.setCommunicationSw(Boolean.TRUE);
            accounts.setUpdatedAt(LocalDateTime.now());
            accountsRepository.save(accounts);
            isUpdated = true;
        }
        return isUpdated;
    }

}
