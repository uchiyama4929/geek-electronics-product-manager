package com.example.demo.controller;

import com.example.demo.controller.web.RootController;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RootControllerTest {

    /**
     * test case 51
     */
    @Test
    public void testIndex_normal() {
        RootController controller = new RootController();
        String result = controller.index();
        assertEquals("redirect:/product/index", result);
    }
}
