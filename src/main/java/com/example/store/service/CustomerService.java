package com.example.store.service;

import com.example.store.dto.CreateCustomerRequestDTO;
import com.example.store.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    List<CustomerDTO> getAllCustomers(String name);

    CustomerDTO createCustomer(CreateCustomerRequestDTO customerDTO);
}
