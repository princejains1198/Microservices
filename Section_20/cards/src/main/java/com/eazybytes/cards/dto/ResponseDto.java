package com.eazybytes.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(
        name = "Response",
        description = "Response DTO represents the status of an operation including status code and message."
)
public class ResponseDto {

    @Schema(
            description = "Status code of the operation"
    )
    private String statusCode;

    @Schema(
            description = "Status message of the operation"
    )
    private String statusMsg;
}
