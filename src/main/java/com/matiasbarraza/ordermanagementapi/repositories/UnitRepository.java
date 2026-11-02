package com.matiasbarraza.ordermanagementapi.repositories;

import com.matiasbarraza.ordermanagementapi.domain.entities.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitRepository extends JpaRepository<Unit, String> {
}
