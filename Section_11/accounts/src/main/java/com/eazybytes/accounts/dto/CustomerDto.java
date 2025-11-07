package com.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(
        name = "Customer",
        description = "Customer DTO represents customer details including name, email, mobile number, and associated accounts."
)
public class CustomerDto {

    @Schema(
            description = "Name of the customer",
            example = "John Doe"
    )
    @NotEmpty(message = "Name cannot be null or empty")
    @Size(min = 5, max = 30, message = "The length of the customer name should be between 5 and 30 characters")
    private String name;

    @Schema(
            description = "Email address of the customer",
            example = "tutor@gmail.com"
    )
    @NotEmpty(message = "Email cannot be null or empty")
    @Email(message = "Please provide a valid email address")
    private String email;

    @Schema(
            description = "Mobile number of the customer",
            example = "9876543210"
    )
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @Schema(
            description = "Accounts associated with the customer",
            implementation = AccountsDto.class,
            example = "{ \"accountNumber\": 1234567890, \"accountType\": \"Savings\", \"branchAddress\": \"123 Main St, Cityville\" }"
    )
    private AccountsDto accountsDto;
}
