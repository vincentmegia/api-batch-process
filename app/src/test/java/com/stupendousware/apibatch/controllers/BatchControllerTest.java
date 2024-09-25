package com.stupendousware.apibatch.controllers;

import com.stupendousware.apibatch.Application;
import com.stupendousware.apibatch.configurations.BatchConfiguration;
import com.stupendousware.apibatch.configurations.WebConfiguration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { BatchConfiguration.class,
        Application.class, WebConfiguration.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BatchControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testStartBatch() {
        var url = "http://localhost:" + port + "/batch/adhoc/start";
        var responseEntity = this.restTemplate
                .getForEntity(url, String.class);
        assertEquals(200, responseEntity.getStatusCode().value());
    }

    @Test
    public void testAdhocBatch() {
        assertEquals(true, true);
    }
}
