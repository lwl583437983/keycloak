package com.remote.lock.remotelock.config;

import com.remote.lock.remotelock.api.KeycloakApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ApiConfig {

  @Value("${keycloak.host}")
  private String serviceHost;

  @Bean
  public KeycloakApi keycloakApi(WebClient.Builder webClientBuilder) {
    WebClient webClient = webClientBuilder.baseUrl(serviceHost).build();
    return HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient))
        .build().createClient(KeycloakApi.class);
  }
}
