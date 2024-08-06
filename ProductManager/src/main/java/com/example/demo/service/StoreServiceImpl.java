package com.example.demo.service;

import com.example.demo.entity.Store;
import com.example.demo.form.StoreForm;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.demo.repository.StoreRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {
    private final StoreRepository storeRepository;

    public StoreServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    /**
     * {@inheritDoc}
     */
    public List<Store> findAll() {
        return storeRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Store findById(Long id) {
        return storeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Store save(StoreForm storeForm) {
        Store store = storeRepository.findById(storeForm.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        store.setId(storeForm.getId());
        store.setName(storeForm.getName());
        store.setAddress(storeForm.getAddress());
        storeRepository.save(store);
        return store;
    }

}
