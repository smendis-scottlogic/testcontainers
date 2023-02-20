package com.smendis.testcontainers.models;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    private UUID id;

    private String name;

    private Date startDate;
}
