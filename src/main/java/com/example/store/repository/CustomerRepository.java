package com.example.store.repository;

import com.example.store.entity.Customer;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByNameContainingIgnoreCase(String name);

    @Query(
            value = "SELECT c.id FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))",
            countQuery = "SELECT COUNT(c) FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))"
    )
    Page<Long> findCustomerIds(@Param("name") String name, Pageable pageable);

    @Query("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.orders WHERE c.id IN :ids")
    List<Customer> findByIdsWithOrders(@Param("ids") List<Long> ids);

}
