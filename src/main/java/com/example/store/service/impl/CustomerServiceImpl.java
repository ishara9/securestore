package com.example.store.service.impl;

import com.example.store.dto.CreateCustomerRequestDTO;
import com.example.store.dto.CustomerDTO;
import com.example.store.dto.PageResponse;
import com.example.store.entity.Customer;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;
import com.example.store.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;


    @Override
    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers(String name) {
        List<Customer> customers = customerRepository.findByNameContainingIgnoreCase(name != null ? name : "");
        return customerMapper.customersToCustomerDTOs(customers);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CustomerDTO> getAllCustomers(String name, Pageable pageable) {
        Page<Long> customerIds = customerRepository.findCustomerIds(name != null ? name : "", pageable);
        List<Customer> customers = customerRepository.findByIdsWithOrders(customerIds.getContent());

        Map<Long, Customer> customerMap = customers.stream()
                .collect(Collectors.toMap(Customer::getId, c -> c));

        List<Customer> collect = customerIds.getContent().stream()
                .map(customerMap::get)
                .collect(Collectors.toList());

        List<CustomerDTO> customerDTOS = customerMapper.customersToCustomerDTOs(collect);
        return new PageResponse<>(customerDTOS, pageable.getPageNumber(), pageable.getPageSize(), customerIds.getTotalElements());
    }

    @Override
    @Transactional
    public CustomerDTO createCustomer(CreateCustomerRequestDTO customerDTO) {
        Customer customer = customerMapper.createCustomerRequestDTOToCustomer(customerDTO);
        return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
    }
}
