package com.example.demo.dto;

import java.util.List;

public record ApiResponseDTO<T>(
        String status,
        String message,
        List<T> results
) {
}
