package com.eazybytes.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(
        name = "Cards",
        description = "Data Transfer Object for Card details"
)
public class CardsDto {

    @NotEmpty(message = "Mobile number cannot be null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    @Schema(
            description = "Mobile number associated with the card",
            example = "1234567890"
    )
    private String mobileNumber;

    @NotEmpty(message = "Card number cannot be empty")
    @Pattern(regexp = "(^$|[0-9]{12})", message = "Card number must be 12 digits")
    @Schema(
            description = "Card number",
            example = "123456789012"
    )
    private String cardNumber;

    @NotEmpty(message = "Card type cannot be null or empty")
    @Schema(
            description = "Type of the card (e.g., CREDIT, DEBIT)",
            example = "CREDIT CARD"
    )
    private String cardType;

    @Positive(message = "Total card limit should be greater than zero")
    @Schema(
            description = "Total limit of the card",
            example = "50000"
    )
    private int totalLimit;

    @PositiveOrZero(message = "Total amount used should be equal or greater than zero")
    @Schema(
            description = "Amount used from the total limit",
            example = "10000"
    )
    private int amountUsed;

    @PositiveOrZero(message = "Total available amount should be equal or greater than zero")
    @Schema(
            description = "Available amount from the total limit",
            example = "40000"
    )
    private int availableAmount;

}

