package com.remote.lock.remotelock.controller;

import com.remote.lock.remotelock.api.KeycloakApi;
import com.remote.lock.remotelock.dto.TokenDto;
import com.remote.lock.remotelock.dto.request.LoginReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

  @Autowired
  private KeycloakApi keycloakApi;

  @Value("${keycloak.client.id}")
  private String clientId;

  @Value("${keycloak.client.secret}")
  private String secret;

  @Operation(summary = "Login token API")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "请求成功"),
      @ApiResponse(responseCode = "400", description = "请求参数没填好"),
      @ApiResponse(responseCode = "401", description = "没有权限"),
      @ApiResponse(responseCode = "403", description = "禁止访问"),
      @ApiResponse(responseCode = "404", description = "请求路径没有或页面跳转路径不对")
  })
  @PostMapping("/login")
  public String login(@RequestBody LoginReq loginReq) {
    Map<String, String> request = generateRequest(loginReq);
    TokenDto response = keycloakApi.getToken(request);
    return response.getAccessToken();
  }

  private Map<String, String> generateRequest(LoginReq loginReq) {
    Map<String, String> keycloakReq = new HashMap<>();
    keycloakReq.put("client_id", clientId);
    keycloakReq.put("client_secret", secret);
    keycloakReq.put("grant_type", "password");
    keycloakReq.put("username", loginReq.getUserName());
    keycloakReq.put("password", loginReq.getPassword());
    return keycloakReq;
  }
}
