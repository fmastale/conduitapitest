package com.griddynamics.conduit.test;


import static com.griddynamics.conduit.helpers.Endpoint.USERS_LOGIN;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;
import static com.griddynamics.conduit.helpers.StatusCode.CODE_200;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.griddynamics.conduit.jsons.UserRequest;
import com.griddynamics.conduit.jsonsdtos.UserRequestDto;
import com.griddynamics.conduit.jsonsdtos.UserResponseDto;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthenticationTest extends ApiTest {
  protected Response response;
  protected UserRequest userBody;
  protected UserRequestDto requestBody;
  protected UserResponseDto userResponseDto;
  protected RequestSpecification requestSpecification;

  @Test
  @DisplayName("Authentication - check if user will be logged and then check his ID")
  void logUserAndGetHisId() {
    // GIVEN
    userBody = new UserRequest(TEST_DATA_PROVIDER.getEmail(), TEST_DATA_PROVIDER.getPassword());
    UserRequestDto requestBody = new UserRequestDto(userBody);

    requestSpecification = RestAssured.given().contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    // WHEN
    response = requestSpecification.post(USERS_LOGIN.getEndpoint());
    userResponseDto = response.as(UserResponseDto.class);

    // THEN
    assertEquals(TEST_DATA_PROVIDER.getUsername(), userResponseDto.user.username,
        "Username is different than expected");
  }

  @Test
  @DisplayName("Authentication - check if user with incorrect password can be log into app")
  void logUserWithIncorrectPassword() {
    // GIVEN
    userBody = new UserRequest(TEST_DATA_PROVIDER.getEmail(), TEST_DATA_PROVIDER.getIncorrectPassword());
    requestBody = new UserRequestDto(userBody);

    requestSpecification = RestAssured.given().contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    //WHEN
    response = requestSpecification.post(USERS_LOGIN.getEndpoint());
    statusCode = response.statusCode();

    //THEN
    assertNotEquals(CODE_200, statusCode, "Actual status code is same as unexpected");

    MatcherAssert.assertThat(statusCode, Matchers.equalTo(CODE_200));
  }

}
