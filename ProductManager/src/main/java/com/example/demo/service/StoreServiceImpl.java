package com.example.demo.service;

import com.example.demo.dto.stores.OrderHistoryDTO;
import com.example.demo.dto.stores.ProductDTO;
import com.example.demo.dto.stores.StoreDTO;
import com.example.demo.entity.Order;
import com.example.demo.entity.ProductPrice;
import com.example.demo.entity.Store;
import com.example.demo.form.StoreForm;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.example.demo.repository.StoreRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<StoreDTO> findAllStoreInfo() {
        List<Store> stores = storeRepository.findAll();

        return stores.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    private StoreDTO convertToDTO(Store store) {
        List<ProductDTO> productDTOs = store.getProductPrices().stream().map(this::convertToProductDTO).collect(Collectors.toList());
        List<OrderHistoryDTO> orderHistoryDTOs = store.getOrders().stream().map(this::convertToOrderHistoryDTO).collect(Collectors.toList());

        return new StoreDTO(store.getName(), store.getAddress(), productDTOs, orderHistoryDTOs);
    }

    /**
     * {@inheritDoc}
     */
    private ProductDTO convertToProductDTO(ProductPrice productPrice) {
        return new ProductDTO(
                productPrice.getProduct().getName(),
                productPrice.getSalePrice(),
                productPrice.getProduct().getProductStocks().getFirst().getStockQuantity()
        );
    }

    /**
     * {@inheritDoc}
     */
    private OrderHistoryDTO convertToOrderHistoryDTO(Order order) {
        return new OrderHistoryDTO(
                order.getProduct().getName(),
                order.getManager().getFullName(),
                order.getOrderQuantity(),
                order.getTotalAmount(),
                order.getCreatedAt()
        );
    }
}
