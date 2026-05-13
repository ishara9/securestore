package com.example.store.controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.dto.PageResponse;
import com.example.store.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/customer")
@RequiredArgsConstructor
public class CustomerControllerV2 {

    private final CustomerService customerService;


    @GetMapping
    @Cacheable(
            value = "customers",
            key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort"
    )
    public PageResponse<CustomerDTO> getAllCustomers(
            @RequestParam(name = "name", required = false) String name,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return customerService.getAllCustomers(name, pageable);
    }
}
