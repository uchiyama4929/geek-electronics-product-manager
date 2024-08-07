package com.example.demo.controller.api;

import com.example.demo.dto.ApiResponseDTO;
import com.example.demo.dto.ErrorInfoDTO;
import com.example.demo.dto.ErrorResponseDTO;
import com.example.demo.dto.ProductCategoryDTO;
import com.example.demo.service.ProductStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductApiController {

    private final ProductStoreService productStoreService;

    @Autowired
    public ProductApiController(ProductStoreService productStoreService) {
        this.productStoreService = productStoreService;
    }

    @GetMapping("/api/products")
    public ResponseEntity<?> products() {
        try {
            List<ProductCategoryDTO> productStoreDtoList = productStoreService.findAllProductStoreInfo();
            ApiResponseDTO<ProductCategoryDTO> response = new ApiResponseDTO<>("1", "", productStoreDtoList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorInfoDTO errorInfo = new ErrorInfoDTO(500);
            ErrorResponseDTO response = new ErrorResponseDTO("0", "internal server error", errorInfo);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

