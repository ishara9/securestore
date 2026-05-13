package com.example.store.controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.dto.PageResponse;
import com.example.store.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerControllerV2.class)
class CustomerControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    private CustomerDTO customerDto;

    @BeforeEach
    void setUp() {
        customerDto = new CustomerDTO();
        customerDto.setId(1L);
        customerDto.setName("John Doe");
    }

    @Test
    void testGetAllCustomersV2() throws Exception {
        when(customerService.getAllCustomers(eq(null), any(Pageable.class)))
                .thenReturn(new PageResponse<>(List.of(customerDto), 0, 10, 1L));

        mockMvc.perform(get("/v2/customer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("John Doe"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    void testGetAllCustomersV2_whenNameParamFound_returnName() throws Exception {
        when(customerService.getAllCustomers(eq("John"), any(Pageable.class)))
                .thenReturn(new PageResponse<>(List.of(customerDto), 0, 10, 1L));

        mockMvc.perform(get("/v2/customer?name=John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("John Doe"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }
}