package com.matiasbarraza.ordermanagementapi.repositories;

import com.matiasbarraza.ordermanagementapi.domain.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByDateBetween(LocalDate start, LocalDate end, Pageable pageable);
    Page<Order> findByCorrelative(Integer correlative, Pageable pageable);

}
