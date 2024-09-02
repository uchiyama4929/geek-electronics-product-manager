package com.example.demo.test;

import com.example.demo.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;

public class OrderTestFactory {

    public static Page<Order> createOrderPage() {
        return new PageImpl<>(Collections.emptyList());
    }
}
