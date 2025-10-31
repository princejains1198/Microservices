package com.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(
        name = "Accounts",
        description = "Accounts DTO represents account details including account number, account type, and branch address."
)
public class AccountsDto {

    @Schema(
            description = "Unique account number"
    )
    @NotEmpty(message = "Account Number cannot be null or empty")
    @Pattern(regexp = "[0-9]{10}", message = "Account Number must be 10 digits")
    private Long accountNumber;

    @Schema(
            description = "Type of the account",
            example = "Savings"
    )
    @NotEmpty(message = "Account Type cannot be null or empty")
    private String accountType;

    @Schema(
            description = "Branch address of the account"
    )
    @NotEmpty(message = "Branch Address cannot be null or empty")
    private String branchAddress;
}
