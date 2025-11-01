package com.eazybytes.loans.controller;

import com.eazybytes.loans.Constants.LoansConstants;
import com.eazybytes.loans.dto.ErrorResponseDto;
import com.eazybytes.loans.dto.LoansContactInfoDto;
import com.eazybytes.loans.dto.LoansDto;
import com.eazybytes.loans.dto.ResponseDto;
import com.eazybytes.loans.service.LoansService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(
        name = "CRUD REST APIs for Loans in EazyBank",
        description = "CRUD REST APIs in EazyBank to CREATE, UPDATE, FETCH AND DELETE loan details"
)
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class LoansController {

    private final LoansService loansService;

    public LoansController(LoansService loansService) {
        this.loansService = loansService;
    }

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private LoansContactInfoDto loansContactInfoDto;

    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Loan created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Invalid input or loan already exists"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error"
            )
    })
    @Operation(
            summary = "Create a new loan",
            description = "Creates a new loan record for the given mobile number"
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createLoan(@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile Number must be 10 digit") String mobileNumber) {
        loansService.createLoan(mobileNumber);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(LoansConstants.STATUS_201, LoansConstants.MESSAGE_201));
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Loan fetched successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Invalid input or loan already exists"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error"
            )
    })
    @Operation(
            summary = "Fetch loan details",
            description = "Fetches the loan details for the given mobile number"
    )
    @GetMapping("/fetch")
    public ResponseEntity<LoansDto> fetchLoanDetails(@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile Number must be 10 digit") String mobileNumber) {
        LoansDto loansDto = loansService.fetchLoansDetails(mobileNumber);
        if(loansDto == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(loansDto);
    }


    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Loan details updated successfully"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed - Update operation failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @Operation(
            summary = "Update loan details REST API",
            description = "Updates the loan details for the given loan information"
    )
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateLoanDetails(@Valid @RequestBody LoansDto loansDto) {
        // Implementation for updating loan details goes here
        boolean isUpdated = loansService.updateLoan(loansDto);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_UPDATE));
        }
    }

    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Loan details deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed - Delete operation failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @Operation(
            summary = "Delete loan details REST API",
            description = "Deletes the loan record for the given mobile number"
    )
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteLoanDetails(@RequestParam @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile Number must be 10 digit") String mobileNumber) {
        // Implementation for deleting loan details goes here
        boolean isDeleted = loansService.deleteLoan(mobileNumber);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(LoansConstants.STATUS_200, LoansConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(LoansConstants.STATUS_417, LoansConstants.MESSAGE_417_DELETE));
        }
    }

    @Operation(
            summary = "Get Build Information REST API",
            description = "API to get the build version information of the Loans Service"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildVersion() {
        return ResponseEntity.status(HttpStatus.OK).body("Loans Microservice Build Version: " + buildVersion);
    }

    @Operation(
            summary = "Get Java Version Information REST API",
            description = "API to get the java version information of the Loans Service"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity.status(HttpStatus.OK).body("Loans Service Java Version: " + environment.getProperty("JAVA_HOME"));
    }

    @Operation(
            summary = "Get Loans Contact Information REST API",
            description = "API to get the contact information for the Loans Service"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/contact-info")
    public ResponseEntity<LoansContactInfoDto> getLoansContactInfo() {
        return ResponseEntity.status(HttpStatus.OK).body(loansContactInfoDto);
    }
}
