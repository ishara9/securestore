package com.example.store.repository;

import com.example.store.entity.Order;
import com.example.store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = "products")
    @Query("SELECT o FROM Order o")
    Page<Order> findAllWithProducts(Pageable pageable);

    @EntityGraph(attributePaths = "products")
    @Query("SELECT o FROM Order o WHERE o.id= :id")
    Optional<Order> findByIdWithProducts(@Param("id") Long id);

}
