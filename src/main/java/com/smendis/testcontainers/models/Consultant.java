package com.smendis.testcontainers.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consultant {
    @Id
    private UUID id;

    private String name;

    private int grade;

    private String technology;
}
