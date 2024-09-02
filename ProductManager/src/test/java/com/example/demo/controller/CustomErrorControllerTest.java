package com.example.demo.controller;

import com.example.demo.controller.web.CustomErrorController;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CustomErrorControllerTest {

    private CustomErrorController customErrorController;
    private HttpServletRequest request;

    @BeforeEach
    public void setup() {
        customErrorController = new CustomErrorController();
        request = mock(HttpServletRequest.class);
    }

    /**
     * test case 6
     */
    @Test
    public void testHandleError_WhenStatusIsNull() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(null);

        String viewName = customErrorController.handleError(request);
        assertEquals("/error/500", viewName);
    }

    /**
     * test case 7
     */
    @Test
    public void testHandleError_Returns403View() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(HttpStatus.FORBIDDEN.value());

        String viewName = customErrorController.handleError(request);
        assertEquals("/error/403", viewName);
    }

    /**
     * test case 8
     */
    @Test
    public void testHandleError_Returns404View() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(HttpStatus.NOT_FOUND.value());

        String viewName = customErrorController.handleError(request);
        assertEquals("/error/404", viewName);
    }

    /**
     * test case 9
     */
    @Test
    public void testHandleError_ReturnsOtherView() {
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR.value());

        String viewName = customErrorController.handleError(request);
        assertEquals("/error/500", viewName);
    }
}
