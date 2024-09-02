package com.example.demo.controller;

import com.example.demo.controller.web.ManufacturerController;
import com.example.demo.entity.Manufacturer;
import com.example.demo.entity.Product;
import com.example.demo.form.ManufacturerForm;
import com.example.demo.service.ManufacturerService;
import com.example.demo.service.ProductService;
import com.example.demo.test.ManufacturerTestFactory;
import com.example.demo.test.ProductTestFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ManufacturerControllerTest {

    private ManufacturerController manufacturerController;
    private ManufacturerService manufacturerService;
    private ProductService productService;
    private Model model;

    @BeforeEach
    public void setup() {
        manufacturerService = mock(ManufacturerService.class);
        productService = mock(ProductService.class);
        model = mock(Model.class);
        manufacturerController = new ManufacturerController(manufacturerService, productService);
    }

    /**
     * test case 21
     */
    @Test
    public void testIndex_normal() {
        Page<Manufacturer> manufacturers = ManufacturerTestFactory.createManufacturerPage();

        when(manufacturerService.findAll(any(PageRequest.class))).thenReturn(manufacturers);

        String viewName = manufacturerController.index(model, 0);
        assertEquals("/manufacturer/index", viewName);
    }

    /**
     * test case 22
     */
    @Test
    public void testCreateGet_normal() {
        String viewName = manufacturerController.create(model);
        assertEquals("/manufacturer/create", viewName);
    }

    /**
     * test case 23
     */
    @Test
    public void testCreatePost_normal() {
        ManufacturerForm manufacturerForm = new ManufacturerForm();
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = manufacturerController.create(manufacturerForm, bindingResult, model);
        assertEquals("redirect:/manufacturer/index", viewName);
    }

    /**
     * test case 24
     */
    @Test
    public void testCreatePost_inValid() {
        ManufacturerForm manufacturerForm = new ManufacturerForm();
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = manufacturerController.create(manufacturerForm, bindingResult, model);
        assertEquals("/manufacturer/create", viewName);
    }

    /**
     * test case 25
     */
    @Test
    public void testDetail_normal() {
        Manufacturer manufacturer = ManufacturerTestFactory.createManufacturer(1L, "Manufacturer A");
        Page<Product> products = ProductTestFactory.createProductPage();

        when(manufacturerService.findById(1L)).thenReturn(manufacturer);
        when(productService.findByManufacturerId(1L, PageRequest.of(0, 10))).thenReturn(products);

        String viewName = manufacturerController.detail(model, 1L, 0);
        assertEquals("/manufacturer/detail", viewName);
    }

    /**
     * test case 26
     */
    @Test
    public void testEditGet_normal() {
        Manufacturer manufacturer = ManufacturerTestFactory.createManufacturer(1L, "Manufacturer A");

        when(manufacturerService.findById(1L)).thenReturn(manufacturer);

        String viewName = manufacturerController.edit(model, 1L);
        assertEquals("/manufacturer/edit", viewName);
    }

    /**
     * test case 27
     */
    @Test
    public void testEditPost_normal() {
        ManufacturerForm manufacturerForm = new ManufacturerForm();
        BindingResult bindingResult = mock(BindingResult.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = manufacturerController.edit(manufacturerForm, bindingResult, model, redirectAttributes, 1L);
        assertEquals("redirect:/manufacturer/detail/{id}", viewName);
    }

    /**
     * test case 28
     */
    @Test
    public void testEditPost_inValid() {
        Manufacturer manufacturer = ManufacturerTestFactory.createManufacturer(1L, "Manufacturer A");
        ManufacturerForm manufacturerForm = new ManufacturerForm();
        manufacturerForm.setId("1");

        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(true);
        when(manufacturerService.findById(1L)).thenReturn(manufacturer);

        String viewName = manufacturerController.edit(manufacturerForm, bindingResult, model, mock(RedirectAttributes.class), 1L);
        assertEquals("/manufacturer/edit", viewName);
    }

    /**
     * test case 29
     */
    @Test
    public void testDelete_normal() {
        String viewName = manufacturerController.delete(1L);
        assertEquals("redirect:/manufacturer/index", viewName);
    }
}