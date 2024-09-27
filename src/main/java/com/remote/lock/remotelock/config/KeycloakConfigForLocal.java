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
@Profile("local")
public class KeycloakConfigForLocal implements WebMvcConfigurer {

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
      config = JsonSerialization.readValue(getClass().getResourceAsStream("/policy-enforcer_local.json"),
          PolicyEnforcerConfig.class);
      //config.setEnforcementMode(PolicyEnforcerConfig.EnforcementMode.DISABLED);
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

/*
  @Bean
  @Order(0)
  SecurityFilterChain staticEndpoints(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/css/**", "/js/**", "/fonts/**", "/images/**", "/i/**", "/resources/**", "/my-image/**")
        .headers((headers) -> headers.cacheControl(HeadersConfigurer.CacheControlConfig::disable))
        .authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll());
    return http.build();
  }
*/

  /*@Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {
    http
        .oauth2Client(withDefaults())
        .oauth2Login((oauth2Login) -> oauth2Login.tokenEndpoint(withDefaults())
            .userInfoEndpoint(withDefaults()))
        .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            (authorizeHttpRequests) -> authorizeHttpRequests
                .requestMatchers("/unauthenticated", "/oauth2/**", "/login/**", "/login.html").permitAll()
                .anyRequest().hasAnyAuthority("OIDC_USER", "ADMIN"))
        .logout((logout) -> logout
            .logoutSuccessUrl(logoutSuccessUrl));
    return http.build();
  }*/

  @Bean
  JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
  }

}
