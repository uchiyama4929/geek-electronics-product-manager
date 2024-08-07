package com.example.demo.service;

import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.PositionRepository;
import com.example.demo.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.demo.entity.Manager;
import com.example.demo.form.ManagerForm;
import com.example.demo.repository.ManagerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Objects;

@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final StoreRepository storeRepository;
    private final PositionRepository positionRepository;
    private final PermissionRepository permissionRepository;

    public ManagerServiceImpl(
            ManagerRepository managerRepository,
            StoreRepository storeRepository,
            PositionRepository positionRepository,
            PermissionRepository permissionRepository) {
        this.managerRepository = managerRepository;
        this.storeRepository = storeRepository;
        this.positionRepository = positionRepository;
        this.permissionRepository = permissionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Manager save(ManagerForm managerForm) {
        Manager manager;

        if (managerForm.getId() != null) {
            // editの場合
            manager = managerRepository.findById(Long.valueOf(managerForm.getId())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager not found"));
            manager.setUpdatedAt(new Date());
            if (!Objects.equals(managerForm.getPassword(), "")) {
                String hashedPassword = hashPassword(managerForm.getPassword());
                manager.setPassword(hashedPassword);
            }
        } else {
            // createの場合
            manager = new Manager();
            manager.setCreatedAt(new Date());
            manager.setUpdatedAt(new Date());
            String hashedPassword = hashPassword(managerForm.getPassword());
            manager.setPassword(hashedPassword);
        }

        manager.setStore(storeRepository.findById(Long.parseLong(managerForm.getStoreId())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Store not found")));
        manager.setPosition(positionRepository.findById(Long.parseLong(managerForm.getPositionId())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Position not found")));
        manager.setPermission(permissionRepository.findById(Long.parseLong(managerForm.getPermissionId())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Permission not found")));

        manager.setLastName(managerForm.getLastName());
        manager.setFirstName(managerForm.getFirstName());
        manager.setEmail(managerForm.getEmail());
        manager.setPhoneNumber(managerForm.getPhoneNumber());
        managerRepository.save(manager);
        return manager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String hashPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Manager> findAll(Pageable pageable) {
        return managerRepository.findAll(pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Manager findById(Long id) {
        return managerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Manager findByEmail(String email) {
        return managerRepository.findByEmail(email);
    }
}
