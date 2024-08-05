package com.example.demo.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class StoreForm implements Serializable {
    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String address;
}


