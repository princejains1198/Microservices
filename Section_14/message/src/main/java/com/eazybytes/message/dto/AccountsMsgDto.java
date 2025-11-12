package com.eazybytes.message.dto;

/**
 * Data Transfer Object for Account Messages
 */
public record AccountsMsgDto(Long accountNumber, String name, String email, String mobileNumber) {
}
