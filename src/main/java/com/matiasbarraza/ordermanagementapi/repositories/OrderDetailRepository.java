package com.matiasbarraza.ordermanagementapi.repositories;

import com.matiasbarraza.ordermanagementapi.domain.entities.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
