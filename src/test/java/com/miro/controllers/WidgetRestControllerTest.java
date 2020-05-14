package com.miro.controllers;

import com.miro.model.Widget;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WidgetRestControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Order(1)
    void createWidget() {
        ResponseEntity<Widget> response = this.restTemplate.postForEntity("/api/v1/widgets/", Widget.builder().zIndex(0).build(), Widget.class);
        assertEquals(CREATED, response.getStatusCode());
    }


    @Test
    @Order(2)
    void getWidgetById() {
        ResponseEntity<Widget> response = this.restTemplate.getForEntity("/api/v1/widgets/0", Widget.class);
        assertNotNull(response.getBody());
        assertEquals(response.getBody().getZIndex(), 0);
        assertEquals(OK, response.getStatusCode());
    }

    @Test
    @Order(3)
    void getWidgets() {
        ResponseEntity<Map> response = this.restTemplate.getForEntity("/api/v1/widgets/", Map.class);
        assertNotNull(response.getBody());
        assertEquals(response.getBody().get("totalElements"), 1);
        assertEquals(OK, response.getStatusCode());
    }

    @Test
    @Order(4)
    void deleteWidget() {
        this.restTemplate.delete("/api/v1/widgets/0");
        ResponseEntity<Map> response = this.restTemplate.getForEntity("/api/v1/widgets/", Map.class);
        assertNotNull(response.getBody());
        assertEquals(response.getBody().get("totalElements"), 0);
        assertEquals(OK, response.getStatusCode());
    }

}