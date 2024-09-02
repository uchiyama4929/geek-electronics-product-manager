package com.example.demo.test;

import com.example.demo.entity.Store;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class StoreTestFactory {

    public static Store createStore(Long id, String name, String address) {
        Store store = new Store();
        store.setId(id);
        store.setName(name);
        store.setAddress(address);
        store.setCreatedAt(new Date());
        store.setUpdatedAt(new Date());
        return store;
    }

    public static List<Store> createStoreList() {
        return Collections.emptyList();
    }
}
