package com.remote.lock.remotelock.config;

import org.keycloak.adapters.authorization.integration.jakarta.ServletPolicyEnforcerFilter;
import org.keycloak.adapters.authorization.spi.ConfigurationResolver;
import org.keycloak.adapters.authorization.spi.HttpRequest;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.keycloak.util.JsonSerialization;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@Profile("dev")
public class KeycloakConfig implements WebMvcConfigurer {

  @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
  String jwkSetUri;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests((authorize) -> authorize
            .requestMatchers("/login/**").permitAll()
            .anyRequest().authenticated()
        ).csrf(AbstractHttpConfigurer::disable)
        .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
        .addFilterAfter(createPolicyEnforcerFilterForDev(), BearerTokenAuthenticationFilter.class);
    return http.build();
  }

  private ServletPolicyEnforcerFilter createPolicyEnforcerFilterForDev() {
    PolicyEnforcerConfig config;
    try {
      config = JsonSerialization.readValue(getClass().getResourceAsStream("/policy-enforcer_dev.json"),
          PolicyEnforcerConfig.class);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new ServletPolicyEnforcerFilter(new ConfigurationResolver() {
      @Override
      public PolicyEnforcerConfig resolve(HttpRequest request) {
        return config;
      }
    });
  }

  @Bean
  JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
  }

}
