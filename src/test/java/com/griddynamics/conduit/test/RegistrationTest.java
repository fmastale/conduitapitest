package com.griddynamics.conduit.test;

import static com.griddynamics.conduit.helpers.Endpoint.USERS;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.jsons.RegistrationRequestUser;
import com.griddynamics.conduit.jsons.UnprocessableEntityError;
import com.griddynamics.conduit.jsonsdtos.RegistrationRequestUserDto;
import com.griddynamics.conduit.jsonsdtos.UnprocessableEntityErrorDto;
import com.griddynamics.conduit.jsonsdtos.UserResponseDto;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RegistrationTest {
  private TestDataProvider testDataProvider = new TestDataProvider();
  private Response response;
  private RegistrationRequestUser user;
  private UserResponseDto responseBody;
  private RegistrationRequestUserDto requestBody;
  private RequestSpecification requestSpecification;

  @BeforeAll
  static void beforeAll() {
    RestAssured.baseURI = Endpoint.BASE_URI.getEndpoint();
  }

  @Test
  @DisplayName("Registration - register user with valid data and check if usernames are equal")
  void registerUserWithValidData() {
    // GIVEN
    user = testDataProvider.getValidRegistrationUser();
    requestBody = new RegistrationRequestUserDto(user);

    requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    // WHEN
    response = requestSpecification.post(USERS.getEndpoint());
    responseBody = response.as(UserResponseDto.class);

    // THEN
    MatcherAssert.assertThat("", responseBody.user.username, Matchers.equalTo(user.username));
  }

  @Test
  @DisplayName("Registration - try to register user with username which is already taken, then check error message")
  void registerUserWithIncorrectData() {
    // GIVEN
    user = new RegistrationRequestUser("adam1234io", "elo123456789a@mail.com", "adam1234");
    requestBody = new RegistrationRequestUserDto(user);

    requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    // WHEN
    response = requestSpecification.post(USERS.getEndpoint());
    UnprocessableEntityErrorDto errorBody = response.as(UnprocessableEntityErrorDto.class);

    //THEN
    MatcherAssert.assertThat("Expected error message is different than actual",
        errorBody.errors.username, Matchers.hasItemInArray("has already been taken"));

  }
}
