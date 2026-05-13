package com.example.store.service;

import com.example.store.dto.CreateCustomerRequestDTO;
import com.example.store.dto.CustomerDTO;
import com.example.store.dto.PageResponse;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {

    List<CustomerDTO> getAllCustomers(String name);

    PageResponse<CustomerDTO> getAllCustomers(String name, Pageable pageable);

    CustomerDTO createCustomer(CreateCustomerRequestDTO customerDTO);
}
