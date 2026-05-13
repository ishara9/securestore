package com.example.store.service.impl;

import com.example.store.dto.CreateCustomerRequestDTO;
import com.example.store.dto.CustomerDTO;
import com.example.store.dto.PageResponse;
import com.example.store.entity.Customer;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void testGetAllCustomers_whenNameNull_usesEmptyFilter() {
        List<Customer> customers = List.of(new Customer());
        List<CustomerDTO> customerDTOs = List.of(new CustomerDTO());

        when(customerRepository.findByNameContainingIgnoreCase("")).thenReturn(customers);
        when(customerMapper.customersToCustomerDTOs(customers)).thenReturn(customerDTOs);

        List<CustomerDTO> result = customerService.getAllCustomers(null);

        assertSame(customerDTOs, result);
        verify(customerRepository).findByNameContainingIgnoreCase("");
    }

    @Test
    void testGetAllCustomers_whenNameProvided_usesNameFilter() {
        List<Customer> customers = List.of(new Customer());
        List<CustomerDTO> customerDTOs = List.of(new CustomerDTO());

        when(customerRepository.findByNameContainingIgnoreCase("john")).thenReturn(customers);
        when(customerMapper.customersToCustomerDTOs(customers)).thenReturn(customerDTOs);

        List<CustomerDTO> result = customerService.getAllCustomers("john");

        assertSame(customerDTOs, result);
        verify(customerRepository).findByNameContainingIgnoreCase("john");
    }

    @Test
    void testGetAllCustomersPageable_returnsPageResponse() {
        Pageable pageable = PageRequest.of(0, 10);
        Customer customer1 = new Customer();
        customer1.setId(2L);
        Customer customer2 = new Customer();
        customer2.setId(1L);
        Page<Long> customerIds = new PageImpl<>(List.of(1L, 2L), pageable, 2);
        List<Customer> customers = List.of(customer1, customer2);
        List<CustomerDTO> customerDTOs = List.of(new CustomerDTO(), new CustomerDTO());

        when(customerRepository.findCustomerIds("john", pageable)).thenReturn(customerIds);
        when(customerRepository.findByIdsWithOrders(List.of(1L, 2L))).thenReturn(customers);
        when(customerMapper.customersToCustomerDTOs(any())).thenReturn(customerDTOs);

        PageResponse<CustomerDTO> result = customerService.getAllCustomers("john", pageable);

        assertSame(customerDTOs, result.content());
        assertEquals(0, result.page());
        assertEquals(10, result.size());
        assertEquals(2L, result.totalElements());
        verify(customerRepository).findCustomerIds("john", pageable);
        verify(customerRepository).findByIdsWithOrders(List.of(1L, 2L));
    }

    @Test
    void testCreateCustomer_mapsAndSavesCustomer() {
        CreateCustomerRequestDTO requestDTO = new CreateCustomerRequestDTO("John Doe");
        Customer toSave = new Customer();
        Customer saved = new Customer();
        CustomerDTO customerDTO = new CustomerDTO();

        when(customerMapper.createCustomerRequestDTOToCustomer(requestDTO)).thenReturn(toSave);
        when(customerRepository.save(toSave)).thenReturn(saved);
        when(customerMapper.customerToCustomerDTO(saved)).thenReturn(customerDTO);

        CustomerDTO result = customerService.createCustomer(requestDTO);

        assertSame(customerDTO, result);
        verify(customerMapper).createCustomerRequestDTOToCustomer(eq(requestDTO));
        verify(customerRepository).save(toSave);
        verify(customerMapper).customerToCustomerDTO(saved);
    }
}