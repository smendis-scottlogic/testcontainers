package com.smendis.testcontainers.repositories;

import com.smendis.testcontainers.models.Consultant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ConsultantRepository extends CrudRepository<Consultant, UUID> {
    @Query(value = "SELECT * FROM Consultant c WHERE c.grade = 2 AND c.technology = :tech", nativeQuery = true)
    List<Consultant> getSeniorConsulantsByTechnology(@Param("tech") String technology);
}
