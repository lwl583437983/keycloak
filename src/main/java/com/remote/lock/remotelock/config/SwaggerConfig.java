package com.remote.lock.remotelock.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Remote lock demo API")
            .description("Remote lock demo API")
            .version("v1"))
        .externalDocs(new ExternalDocumentation()
            .description("Remote lock demo API doc")
            .url("/"));
  }
}
