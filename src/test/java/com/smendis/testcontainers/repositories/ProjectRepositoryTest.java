package com.smendis.testcontainers.repositories;

import com.smendis.testcontainers.models.Consultant;
import com.smendis.testcontainers.models.Project;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataMongoTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProjectRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private ProjectRepository repository;

    @Test
    @Order(1)
    public void initial_project_count_should_be_zero(){
        List<Project> projects = new ArrayList<>();
        repository.findAll().forEach(p -> projects.add(p));
        Assertions.assertThat(projects).hasSize(0);
    }

    @Test
    @Order(2)
    @Rollback(value = false)
    public void should_be_able_to_save_projects(){
        Project project1 = new Project(UUID.randomUUID(), "Primary School Attendance", new Date(2022, 12, 15));
        Project project2 = new Project(UUID.randomUUID(), "Pharmacy Inventory System", new Date(2023, 4, 1));
        Project savedProject1 = repository.save(project1);
        Project savedProject2 = repository.save(project2);
        Assertions.assertThat(savedProject1.getName()).isEqualTo(project1.getName());
        Assertions.assertThat(savedProject2.getName()).isEqualTo(project2.getName());
    }

    @Test
    @Order(3)
    public void should_be_able_to_get_project_that_are_already_started_as_at_given_date(){
        List<Project> projects = new ArrayList<>();
        repository.getProjectsThatAreStartedBefore(new Date(2023,2,20)).forEach(p -> projects.add(p));

        Assertions.assertThat(projects).hasSize(1);
        Assertions.assertThat(projects.get(0).getName()).isEqualTo("Primary School Attendance");
    }
}