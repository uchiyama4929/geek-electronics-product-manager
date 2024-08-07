package com.example.demo.dto;

public record ErrorResponseDTO(
        String status,
        String message,
        ErrorInfoDTO error
) {
}

