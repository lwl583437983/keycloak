package com.remote.lock.remotelock.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
public class RemoteLockControllerTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  public void testDemo() {
    String result = webTestClient.get().uri("/demo")
        .exchange()
        .expectStatus()
        .is2xxSuccessful()
        .expectBody(String.class)
        .returnResult()
        .getResponseBody();
    assertEquals("Demo API for remote lock", result);
  }

}
