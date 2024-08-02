package com.example.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrderForm implements Serializable {

    @NotNull
    private Long productId;

    @NotBlank
    private String quantity;
}
