package com.example.demo.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class ManufacturerForm implements Serializable {
    private String id;

    @NotBlank
    private String name;
}
