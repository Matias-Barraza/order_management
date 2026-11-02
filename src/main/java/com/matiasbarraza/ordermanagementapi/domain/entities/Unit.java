package com.matiasbarraza.ordermanagementapi.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "units")
public class Unit {

    @Id
    @Column(length = 3)
    private String id;

    @Column(name = "description", length = 50, nullable = false, unique = true)
    private String description;

}