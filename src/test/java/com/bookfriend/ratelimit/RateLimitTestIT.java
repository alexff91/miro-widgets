package com.bookfriend.ratelimit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static com.marcosbarbero.cloud.autoconfigure.zuul.ratelimit.support.RateLimitConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RateLimitTestIT {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void testNotExceedingCapacityRequest() {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/api/v1/books/1", String.class);
        HttpHeaders headers = response.getHeaders();
        assertHeaders(headers, "rate-limit-application_default_127.0.0.1");
        assertEquals(NOT_FOUND, response.getStatusCode());
    }


    @Test
    public void testNoRateLimit() {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/api/v1/books/1", String.class);
        HttpHeaders headers = response.getHeaders();
        assertHeaders(headers, "rate-limit-application_default_127.0.0.1");
        assertEquals(NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testMultipleUrls() {
        for (int i = 0; i < 12; i++) {
            ResponseEntity<String> response = this.restTemplate.getForEntity("/api/v1/books/1", String.class);
            HttpHeaders headers = response.getHeaders();
            assertHeaders(headers, "rate-limit-application_default_127.0.0.1");
            assertEquals(NOT_FOUND, response.getStatusCode());
        }
    }

    @Test
    public void testExceedingRateLimit() {
        ResponseEntity<String> response = this.restTemplate.getForEntity("/api/v1/books", String.class);
        HttpHeaders headers = response.getHeaders();
        String key = "rate-limit-application_booksall_127.0.0.1";
        assertEquals(OK, response.getStatusCode());

        for (int i = 0; i < 300; i++) {
            response = this.restTemplate.getForEntity("/api/v1/books", String.class);
        }
        assertEquals(TOO_MANY_REQUESTS, response.getStatusCode());
    }

    private void assertHeaders(HttpHeaders headers, String key) {
        if (key != null && !key.startsWith("-")) {
            key = "-" + key;
        } else if (key == null) {
            key = "";
        }

        String limit = headers.getFirst(HEADER_LIMIT + key);
        String remaining = headers.getFirst(HEADER_REMAINING + key);
        String reset = headers.getFirst(HEADER_RESET + key);

        assertNotNull(limit);
        assertNotNull(remaining);
        assertNotNull(reset);
    }
}