package com.griddynamics.conduit.helpers;

import static com.griddynamics.conduit.helpers.Endpoint.USERS_LOGIN;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;

import com.griddynamics.conduit.jsons.UserRequest;
import com.griddynamics.conduit.jsonsdtos.UserRequestDto;
import com.griddynamics.conduit.jsonsdtos.UserResponseDto;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;


public class TokenProvider {
  private UserRequestDto requestBody;
  private UserResponseDto userResponseDto;
  private RequestSpecification requestSpecification;

  public TokenProvider() {
    RestAssured.baseURI = Endpoint.BASE_URI.getEndpoint();
  }

  public String getTokenForUser(UserRequest user) {
    this.userResponseDto = logUser(user);
    return "Token " + userResponseDto.user.token;
  }

  private UserResponseDto logUser(UserRequest user) {
    requestBody = new UserRequestDto(user);

    requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON.getDetails()).body(requestBody);

    userResponseDto =
        requestSpecification.post(USERS_LOGIN.getEndpoint()).as(UserResponseDto.class);

    return userResponseDto;
  }
}
