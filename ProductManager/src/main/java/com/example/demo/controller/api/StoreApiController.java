package com.example.demo.controller.api;

import com.example.demo.dto.ApiResponseDTO;
import com.example.demo.dto.ErrorResponseDTO;
import com.example.demo.dto.stores.StoreDTO;
import com.example.demo.dto.ErrorInfoDTO;
import com.example.demo.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StoreApiController {
    private final StoreService storeService;

    public StoreApiController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/api/stores")
    public ResponseEntity<?> getStores() {
        try {
            List<StoreDTO> storeList = storeService.findAllStoreInfo();
            ApiResponseDTO<StoreDTO> response = new ApiResponseDTO<>("1", "", storeList);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ErrorInfoDTO errorInfo = new ErrorInfoDTO(500);
            ErrorResponseDTO response = new ErrorResponseDTO("0", "internal server error", errorInfo);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
