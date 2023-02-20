package com.smendis.testcontainers.repositories;

import com.smendis.testcontainers.models.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends MongoRepository<Project, UUID> {
    @Query(value = "{ 'startDate': { '$lte' : ?0 }}")
    List<Project> getProjectsThatAreStartedBefore(Date beforeDate);
}
