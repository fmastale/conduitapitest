package com.griddynamics.conduit.test;

import static com.griddynamics.conduit.helpers.Endpoint.USERS_LOGIN;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.jsons.UserRequest;
import com.griddynamics.conduit.jsonsdtos.UserRequestDto;
import com.griddynamics.conduit.jsonsdtos.UserResponseDto;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class ApiTest {
  protected static String TOKEN;
  protected static final TestDataProvider TEST_DATA_PROVIDER = new TestDataProvider();

  protected int statusCode;

  protected UserRequest userBody;
  protected UserRequestDto requestBody;
  protected UserResponseDto userResponseDto;
  protected RequestSpecification requestSpecification;

  public ApiTest() {
    RestAssured.baseURI = Endpoint.BASE_URI.getEndpoint();

    TOKEN = getToken(TEST_DATA_PROVIDER.getEmail(), TEST_DATA_PROVIDER.getPassword());
  }

  protected UserResponseDto logUser(String email, String password) {
    userBody = new UserRequest(email, password);
    requestBody = new UserRequestDto(userBody);

    requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    userResponseDto =
        requestSpecification.post(USERS_LOGIN.getEndpoint()).as(UserResponseDto.class);

    return userResponseDto;
  }

  private String getToken(String email, String password) {
    this.userResponseDto = logUser(email, password);
    return "Token " + userResponseDto.user.token;
  }
}
