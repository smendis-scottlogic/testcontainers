package com.smendis.testcontainers.repositories;

import com.smendis.testcontainers.models.Consultant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour
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
    public void should_be_able_to_get_senior_consultant_by_technology(){
        // arrange
        Consultant consultant1 = new Consultant(UUID.randomUUID(), "Adam Smith", 2, "Java");
        Consultant consultant2 = new Consultant(UUID.randomUUID(), "Kim James", 2, ".NET");
        Consultant savedConsultant1 = repository.save(consultant1);
        Consultant savedConsultant2 = repository.save(consultant2);
        // act
        List<Consultant> consultants = new ArrayList<>();
        repository.getSeniorConsulantsByTechnology("Java").forEach(c -> consultants.add(c));;
        // assert
        Assertions.assertThat(consultants).hasSize(1);
        Assertions.assertThat(consultants.get(0).getName()).isEqualTo("Adam Smith");
    }
}