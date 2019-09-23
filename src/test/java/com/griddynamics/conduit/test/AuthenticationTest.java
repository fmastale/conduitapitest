package com.griddynamics.conduit.test;


import static com.griddynamics.conduit.helpers.Endpoint.USERS;
import static com.griddynamics.conduit.helpers.Endpoint.USERS_LOGIN;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;
import static com.griddynamics.conduit.helpers.StatusCode.CODE_200;
import static com.griddynamics.conduit.helpers.StatusCode.CODE_422;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.jsons.RegistrationRequestUser;
import com.griddynamics.conduit.jsons.UserRequest;
import com.griddynamics.conduit.jsonsdtos.RegistrationRequestUserDto;
import com.griddynamics.conduit.jsonsdtos.UnprocessableEntityErrorDto;
import com.griddynamics.conduit.jsonsdtos.UserRequestDto;
import com.griddynamics.conduit.jsonsdtos.UserResponseDto;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthenticationTest {
  private static Response response;
  private static UserResponseDto userResponseDto;
  private static RequestSpecification requestSpecification;
  private static RegistrationRequestUser user = new TestDataProvider().getValidRegistrationUser();


  private int statusCode;

  private UserRequest userBody;
  private UserRequestDto requestBody;
  private TestDataProvider testDataProvider = new TestDataProvider();

  @BeforeAll
  static void beforeAll() {
    RestAssured.baseURI = Endpoint.BASE_URI.getEndpoint();

    registerUser(user);
  }

  @Test
  @DisplayName("Authentication - check if user will be logged and then check his ID")
  void logUserAndGetHisId() {
    // GIVEN
    userBody = new UserRequest(user.email, user.password);
    UserRequestDto requestBody = new UserRequestDto(userBody);

    requestSpecification = RestAssured.given()
        .contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    // WHEN
    response = requestSpecification.post(USERS_LOGIN.getEndpoint());
    userResponseDto = response.as(UserResponseDto.class);

    // THEN
    MatcherAssert.assertThat("Username is different than expected",
        userResponseDto.user.username, Matchers.equalTo(user.username));
  }

  @Test
  @DisplayName("Authentication - check if user with incorrect password can be log into app")
  void logUserWithIncorrectPassword() {
    // GIVEN
    userBody = new UserRequest(user.email, testDataProvider.getIncorrectPassword());
    requestBody = new UserRequestDto(userBody);

    requestSpecification = RestAssured.given()
        .contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    //WHEN
    response = requestSpecification.post(USERS_LOGIN.getEndpoint());
    statusCode = response.statusCode();

    //THEN
    MatcherAssert.assertThat("Actual status code is not the one we expected",
        statusCode, Matchers.not(CODE_200.getValue()));
  }
  
  @Test
  @DisplayName("Authentication - check if user with correct email and empty password can be logged into app")
  void logUserWithEmptyPassword() {
    // GIVEN
    userBody = new UserRequest(user.email, "");
    requestBody = new UserRequestDto(userBody);

    requestSpecification = RestAssured.given()
        .contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    // WHEN
    response = requestSpecification.post(USERS_LOGIN.getEndpoint());
    statusCode = response.getStatusCode();

    //THEN
    MatcherAssert.assertThat("Expected status code is different than actual",
        statusCode, Matchers.equalTo(CODE_422.getValue()));
  }

  @Test
  @DisplayName("Authentication - check if user with correct email can be logged withoud specyfing password")
  void logUserWithoutPassword() {
    userBody = new UserRequest(user.email);
    requestBody = new UserRequestDto(userBody);

    requestSpecification = RestAssured.given()
        .contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    // WHEN
    response = requestSpecification.post(USERS_LOGIN.getEndpoint());
    statusCode = response.getStatusCode();

    //THEN
    MatcherAssert.assertThat("Expected status code is different than actual",
        statusCode, Matchers.equalTo(CODE_422.getValue()));
  }

  @Test
  @DisplayName("Authentication - check error message if user with empty body will be send")
  void checkErrorMessageForUserWithEmptyBody() {
    userBody = new UserRequest();
    requestBody = new UserRequestDto(userBody);

    requestSpecification = RestAssured.given()
        .contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    // WHEN
    response = requestSpecification.post(USERS_LOGIN.getEndpoint());
    UnprocessableEntityErrorDto errorBody = response.as(UnprocessableEntityErrorDto.class);

    //THEN
    MatcherAssert.assertThat("Expected error message is different than actual",
        errorBody.errors.emailOrPassword, Matchers.hasItemInArray("is invalid"));
  }

  private static void registerUser(RegistrationRequestUser user) {
    RegistrationRequestUserDto requestBody = new RegistrationRequestUserDto(user);

    requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    response = requestSpecification.post(USERS.getEndpoint());
    userResponseDto = response.as(UserResponseDto.class);
  }
}

