package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.demo.entity.Order;

public class OrderRepositoryTest {

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByStoreId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> orderPage = new PageImpl<>(Collections.singletonList(new Order()));

        when(orderRepository.findByStoreId(1L, pageable)).thenReturn(orderPage);

        Page<Order> result = orderRepository.findByStoreId(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());

        verify(orderRepository, times(1)).findByStoreId(1L, pageable);
    }
}
