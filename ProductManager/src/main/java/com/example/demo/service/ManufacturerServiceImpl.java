package com.example.demo.service;

import org.springframework.http.HttpStatus;
import com.example.demo.entity.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.example.demo.form.ManufacturerForm;
import org.springframework.data.domain.Pageable;
import com.example.demo.repository.ManufacturerRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {
    private final ManufacturerRepository manufacturerRepository;

    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository) {
        this.manufacturerRepository = manufacturerRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Manufacturer> findAll(Pageable pageable) {
        return manufacturerRepository.findAll(pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Manufacturer save(ManufacturerForm manufacturerForm) {
        Manufacturer manufacturer;

        if (manufacturerForm.getId() != null) {
            // editの場合
            manufacturer = manufacturerRepository.findById(Long.valueOf(manufacturerForm.getId())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manufacturer not found"));
            manufacturer.setUpdatedAt(new Date());
        } else {
            // createの場合
            manufacturer = new Manufacturer();
            manufacturer.setCreatedAt(new Date());
            manufacturer.setUpdatedAt(new Date());
        }
        manufacturer.setName(manufacturerForm.getName());
        manufacturerRepository.save(manufacturer);
        return manufacturer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Manufacturer findById(Long id) {
        return manufacturerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) {
        manufacturerRepository.deleteById(id);
    }

}
