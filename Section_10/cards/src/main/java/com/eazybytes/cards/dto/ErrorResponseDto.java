package com.eazybytes.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(
        name = "ErrorResponse",
        description = "ErrorResponse DTO represents the details of an error including API path, error code, message, and timestamp."
)
public class ErrorResponseDto {
    @Schema(
            description = "API path where the error occurred"
    )
    private String apiPath;

    @Schema(
            description = "HTTP status code of the error"
    )
    private HttpStatus errorCode;

    @Schema(
            description = "Error message describing the error"
    )
    private String errorMsg;

    @Schema(
            description = "Timestamp when the error occurred"
    )
    private LocalDateTime errorTime;

}
