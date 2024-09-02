package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductStockRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductStockRepository productStockRepository;

    @Mock
    private ProductService productService;

    @Mock
    private ManagerService managerService;

    @Mock
    private StoreService storeService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Product product;
    private Manager manager;
    private Store store;
    private ProductStock productStock;
    private Order order;

    @BeforeEach
    public void setUp() {
        product = new Product();
        product.setId(1L);
        product.setCostPrice(100L);

        manager = new Manager();
        manager.setId(1L);

        store = new Store();
        store.setId(1L);

        productStock = new ProductStock();
        productStock.setProduct(product);
        productStock.setStore(store);
        productStock.setStockQuantity(10L);

        order = new Order();
        order.setProduct(product);
        order.setManager(manager);
        order.setStore(store);
        order.setOrderQuantity(5L);
        order.setTotalAmount(500L);
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
    }

    @Test
    @Transactional
    public void testCreateOrder() {
        when(productService.findById(1L)).thenReturn(product);
        when(managerService.findById(1L)).thenReturn(manager);
        when(storeService.findById(1L)).thenReturn(store);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(productStockRepository.findByProductIdAndStoreId(1L, 1L)).thenReturn(productStock);

        Order createdOrder = orderService.createOrder("5", 1L, 1L, 1L);

        assertEquals(5L, createdOrder.getOrderQuantity());
        assertEquals(500L, createdOrder.getTotalAmount());
        assertEquals(product, createdOrder.getProduct());
        assertEquals(manager, createdOrder.getManager());
        assertEquals(store, createdOrder.getStore());

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(productStockRepository, times(1)).save(any(ProductStock.class));
    }

    @Test
    @Transactional
    public void testCreateOrder_ProductNotFound() {
        when(productService.findById(1L)).thenReturn(product);
        when(managerService.findById(1L)).thenReturn(manager);
        when(storeService.findById(1L)).thenReturn(store);
        when(productStockRepository.findByProductIdAndStoreId(1L, 1L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> orderService.createOrder("5", 1L, 1L, 1L));

        verify(orderRepository, never()).save(any(Order.class));
        verify(productStockRepository, never()).save(any(ProductStock.class));
    }


    @Test
    public void testFindByStoreId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> page = new PageImpl<>(Collections.singletonList(order));

        when(orderRepository.findByStoreId(1L, pageable)).thenReturn(page);

        Page<Order> result = orderService.findByStoreId(1L, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(order, result.getContent().getFirst());
        verify(orderRepository, times(1)).findByStoreId(1L, pageable);
    }
}
