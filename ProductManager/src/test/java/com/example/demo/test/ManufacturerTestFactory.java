package com.example.demo.test;

import com.example.demo.entity.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.Date;

public class ManufacturerTestFactory {

    public static Manufacturer createManufacturer(Long id, String name) {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(id);
        manufacturer.setName(name);
        manufacturer.setCreatedAt(new Date());
        manufacturer.setUpdatedAt(new Date());
        return manufacturer;
    }

    public static Page<Manufacturer> createManufacturerPage() {
        return new PageImpl<>(Collections.emptyList());
    }
}
