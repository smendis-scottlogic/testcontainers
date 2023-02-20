package com.smendis.testcontainers.repositories;

import com.smendis.testcontainers.models.Consultant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour
@TestPropertySource("classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ConsultantRepositoryTest {


    @Container
    static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("test")
            .withUsername("sa")
            .withPassword("sa");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    @Autowired
    private ConsultantRepository repository;
    @Test
    @Order(1)
    public void initial_consultant_count_should_be_zero(){
        List<Consultant> consultants = new ArrayList<>();
        repository.findAll().forEach(c -> consultants.add(c));
        Assertions.assertThat(consultants).hasSize(0);
    }
    @Test
    @Order(2)
    @Rollback(value = false)
    public void should_be_able_to_save_consultants(){
        Consultant consultant1 = new Consultant(UUID.randomUUID(), "Adam Smith", 2, "Java");
        Consultant consultant2 = new Consultant(UUID.randomUUID(), "Kim James", 2, ".NET");
        Consultant savedConsultant1 = repository.save(consultant1);
        Consultant savedConsultant2 = repository.save(consultant2);
        Assertions.assertThat(savedConsultant1.getName()).isEqualTo(consultant1.getName());
        Assertions.assertThat(savedConsultant2.getName()).isEqualTo(consultant2.getName());
    }

    @Test
    @Order(3)
    public void should_be_able_to_get_senior_consultant_by_technology(){
        List<Consultant> consultants = new ArrayList<>();
        repository.getSeniorConsulantsByTechnology("Java").forEach(c -> consultants.add(c));;

        Assertions.assertThat(consultants).hasSize(1);
        Assertions.assertThat(consultants.get(0).getName()).isEqualTo("Adam Smith");
    }
}