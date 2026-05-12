package com.example.store.service.impl;

import com.example.store.dto.CreateCustomerRequestDTO;
import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;
import com.example.store.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;


    @Override
    public List<CustomerDTO> getAllCustomers(String name) {
        return customerMapper.customersToCustomerDTOs(customerRepository.findByNameContainingIgnoreCase(name != null ? name : ""));
    }

    @Override
    public CustomerDTO createCustomer(CreateCustomerRequestDTO customerDTO) {
        Customer customer = customerMapper.createCustomerRequestDTOToCustomer(customerDTO);
        return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
    }
}
