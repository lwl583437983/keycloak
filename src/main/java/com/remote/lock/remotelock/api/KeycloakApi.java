package com.remote.lock.remotelock.api;

import com.remote.lock.remotelock.dto.TokenDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Map;

@HttpExchange("/realms/my-realm/protocol/openid-connect")
public interface KeycloakApi {

  @PostExchange(value = "/token", contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  TokenDto getToken(@RequestParam Map<String, String> requestParam);

}
