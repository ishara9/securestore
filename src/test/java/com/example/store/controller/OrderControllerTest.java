package com.example.store.controller;

import com.example.store.dto.OrderDTO;
import com.example.store.dto.OrderCustomerDTO;
import com.example.store.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    private OrderDTO orderDto;

    private OrderCustomerDTO orderCustomerDto;

    @BeforeEach
    void setUp() {
        orderCustomerDto = new OrderCustomerDTO();
        orderCustomerDto.setId(1L);
        orderCustomerDto.setName("John Doe");

        orderDto = new OrderDTO();
        orderDto.setId(1L);
        orderDto.setDescription("Test Order");
        orderDto.setCustomer(orderCustomerDto);
    }

    @Test
    void testCreateOrder() throws Exception {

        when(orderService.createOrder(any())).thenReturn(orderDto);

        String payload = "{\"description\":\"Test Order\",\"customers\":{\"name\":\"John Doe\"},\"products\":[]}";

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Test Order"));
    }

    @Test
    void testGetOrder() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of(orderDto));

        mockMvc.perform(get("/order"))
                .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].description").value("Test Order"))
            .andExpect(jsonPath("$[0].customer.name").value("John Doe"));
    }

}