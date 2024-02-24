package com.ivan.restapplication.integration;

import com.ivan.restapplication.entity.User;
import com.ivan.restapplication.repository.UsersRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public class DatabaseIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withUsername("postgres")
            .withPassword("postgres");

    User user;
    String COUNTRY = "Ukraine";

    @Autowired
    UsersRepository usersRepository;

    @BeforeEach
    public void setUp(){
        postgreSQLContainer.start();
        user = new User(COUNTRY);
    }

    @AfterEach
    public void teardown(){
        postgreSQLContainer.stop();
    }

    @Test
    public void testContainerIsRunning() {
        Assertions.assertTrue(postgreSQLContainer.isRunning());
    }

    @Test
    public void testSaveUser(){
        User result = usersRepository.save(user);

        Assertions.assertTrue(usersRepository.existsById(result.getId()));
        Assertions.assertEquals(COUNTRY, result.getCountry());
    }
}
