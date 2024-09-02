package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.dto.stores.OrderHistoryDTO;
import com.example.demo.dto.stores.ProductDTO;
import com.example.demo.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.dto.stores.StoreDTO;
import com.example.demo.form.StoreForm;
import com.example.demo.repository.StoreRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class StoreServiceImplTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreServiceImpl storeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        List<Store> stores = Collections.singletonList(new Store());
        when(storeRepository.findAll()).thenReturn(stores);

        List<Store> result = storeService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindById_StoreExists() {
        Long storeId = 1L;
        Store store = new Store();
        store.setId(storeId);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

        Store result = storeService.findById(storeId);

        assertNotNull(result);
        assertEquals(storeId, result.getId());
    }

    @Test
    public void testFindById_StoreNotFound() {
        Long storeId = 1L;

        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            storeService.findById(storeId);
        });

        String expectedMessage = "Product not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testSave() {
        StoreForm storeForm = new StoreForm();
        storeForm.setId(1L);
        storeForm.setName("Test Store");
        storeForm.setAddress("123 Test St");

        Store store = new Store();
        store.setId(1L);

        when(storeRepository.findById(storeForm.getId())).thenReturn(Optional.of(store));
        when(storeRepository.save(store)).thenReturn(store);

        Store result = storeService.save(storeForm);

        assertNotNull(result);
        assertEquals(storeForm.getName(), result.getName());
        assertEquals(storeForm.getAddress(), result.getAddress());
    }


    @Test
    public void testFindAllStoreInfo() {
        Date targetDate = new Date();
        Product product = new Product();
        product.setName("Test Product");

        ProductStock productStock = new ProductStock();
        productStock.setStockQuantity(100L);
        product.setProductStocks(Collections.singletonList(productStock));

        ProductPrice productPrice = new ProductPrice();
        productPrice.setProduct(product);
        productPrice.setSalePrice(200L);

        Manager manager = new Manager();
        manager.setFirstName("Manager");
        manager.setLastName("Test");

        Order order = new Order();
        order.setProduct(product);
        order.setManager(manager);
        order.setOrderQuantity(5L);
        order.setTotalAmount(1000L);
        order.setCreatedAt(targetDate);

        Store store = new Store();
        store.setName("Test Store");
        store.setAddress("東京都渋谷区");
        store.setProductPrices(Collections.singletonList(productPrice));
        store.setOrders(Collections.singletonList(order));

        when(storeRepository.findAll()).thenReturn(Collections.singletonList(store));

        List<StoreDTO> result = storeService.findAllStoreInfo();

        assertEquals(1, result.size());
        StoreDTO storeDTO = result.getFirst();
        assertEquals("Test Store", storeDTO.storeName());
        assertEquals("東京都渋谷区", storeDTO.address());

        List<ProductDTO> productDTOList = storeDTO.products();
        assertEquals(1, productDTOList.size());
        ProductDTO productDTO = productDTOList.getFirst();
        assertEquals("Test Product", productDTO.productName());
        assertEquals(200L, productDTO.salesPrice());
        assertEquals(100L, productDTO.stockQuantity());

        List<OrderHistoryDTO> orderHistoryDTOList = storeDTO.orderHistory();
        assertEquals(1, orderHistoryDTOList.size());
        OrderHistoryDTO orderHistoryDTO = orderHistoryDTOList.getFirst();
        assertEquals("Test Product", orderHistoryDTO.productName());
        assertEquals("Test Manager", orderHistoryDTO.managerName());
        assertEquals(5, orderHistoryDTO.orderQuantity());
        assertEquals(1000L, orderHistoryDTO.totalAmount());
        assertEquals(targetDate, orderHistoryDTO.orderDate());
    }
}
