package com.eazybytes.accounts.dto;

import lombok.Data;

@Data
public class CustomerDetailsDto {

    private String name;

    private String email;

    private String mobileNumber;

    private Long accountNumber;

    private String accountType;

    private String branchAddress;
}
