package com.example.store.controller;

import com.example.store.dto.CreateCustomerRequestDTO;
import com.example.store.dto.CustomerDTO;
import com.example.store.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    @Cacheable(
            value = "customers",
            key = "'customer-search-' + #name "
    )
    public List<CustomerDTO> getAllCustomers(@RequestParam(name = "name", required = false) String name) {
        return customerService.getAllCustomers(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CacheEvict(value = "customers", allEntries = true)
    public CustomerDTO createCustomer(@RequestBody CreateCustomerRequestDTO customerDTO) {
        return customerService.createCustomer(customerDTO);
    }
}
