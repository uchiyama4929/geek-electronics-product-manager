package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductStockRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductStockRepository productStockRepository;
    private final ProductService productService;
    private final ManagerService managerService;
    private final StoreService storeService;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            ProductStockRepository productStockRepository,
            ProductService productService,
            ManagerService managerService,
            StoreService storeService
    ) {
        this.orderRepository = orderRepository;
        this.productStockRepository = productStockRepository;
        this.productService = productService;
        this.managerService = managerService;
        this.storeService = storeService;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public Order createOrder(String orderQuantity, Long orderProductId, Long managerId, Long storeId) {
        Product product = productService.findById(orderProductId);
        Manager manager = managerService.findById(managerId);
        Store store = storeService.findById(storeId);

        Long costPrice = product.getCostPrice();
        Order order = new Order();
        order.setProduct(product);
        order.setManager(manager);
        order.setStore(store);
        order.setOrderQuantity(Long.parseLong(orderQuantity));
        order.setTotalAmount(costPrice * Long.parseLong(orderQuantity));
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
        orderRepository.save(order);

        ProductStock productStock = productStockRepository.findByProductIdAndStoreId(orderProductId, store.getId());
        if (productStock != null) {
            productStock.setStockQuantity(productStock.getStockQuantity() + Long.parseLong(orderQuantity));
            productStockRepository.save(productStock);
        } else {
            throw new RuntimeException("Product not found: " + orderProductId);
        }
        return order;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Order> findByStoreId(Long storeId, Pageable pageable) {
        return orderRepository.findByStoreId(storeId, pageable);
    }
}
