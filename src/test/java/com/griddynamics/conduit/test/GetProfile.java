package com.griddynamics.conduit.test;

import static com.griddynamics.conduit.helpers.Endpoint.PROFILES_USERNAME;
import static com.griddynamics.conduit.helpers.Endpoint.USERS;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.USERNAME;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.jsons.GenericError;
import com.griddynamics.conduit.jsons.RegistrationRequestUser;
import com.griddynamics.conduit.jsonsdtos.ProfileDto;
import com.griddynamics.conduit.jsonsdtos.RegistrationRequestUserDto;
import com.griddynamics.conduit.jsonsdtos.UserResponseDto;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GetProfile {
  private static UserResponseDto regularUser;
  private static UserResponseDto maxUsernameUser;
  private static RequestSpecification requestSpecification;

  private ProfileDto responseBody;

  //todo: empty username, maxLength, special chars in username, space inside username

  @BeforeAll
  static void prepareEnvironment() {
    RestAssured.baseURI = Endpoint.BASE_URI.getEndpoint();

    regularUser = registerUser(new TestDataProvider().getValidRegistrationUser());
    maxUsernameUser = registerUser(new TestDataProvider().getUserWithMaxName());
  }

  @Test
  @DisplayName("Get profile for valid user, check username")
  void getValidUserProfile() {
    // GIVEN
    requestSpecification =
        RestAssured.given().pathParam(USERNAME.getDetail(), regularUser.user.username);

    // WHEN
    responseBody =
        requestSpecification.get(PROFILES_USERNAME.getEndpoint()).as(ProfileDto.class);

    // THEN
    MatcherAssert.assertThat(
        "Expected and actual username are different",
        responseBody.profile.username,
        Matchers.equalTo(regularUser.user.username));
  }

  @Test
  @DisplayName("Get profile")
  void getProfileForUserWithMaxUsername () {
    // GIVEN
    requestSpecification =
        RestAssured.given().pathParam(USERNAME.getDetail(), maxUsernameUser.user.username);

    // WHEN
    responseBody =
        requestSpecification.get(PROFILES_USERNAME.getEndpoint()).as(ProfileDto.class);

    // THEN
    MatcherAssert.assertThat(
        "Expected and actual username are different",
        responseBody.profile.username,
        Matchers.equalTo(maxUsernameUser.user.username));
  }

  @Test
  @DisplayName("Get profile of user with random and incorrect username, check error message")
  void getProfileForIncorrectUsername() {
    // GIVEN
    requestSpecification =
        RestAssured.given()
            .pathParam(USERNAME.getDetail(), new TestDataProvider().getRandomIncorrectUsername());

    // WHEN
    GenericError error =
        requestSpecification.get(PROFILES_USERNAME.getEndpoint()).as(GenericError.class);

    // THEN
    MatcherAssert.assertThat(
        "Expected and actual error messages are not the same",
        error.error,
        Matchers.equalTo("Not Found"));
  }

  @Test
  @DisplayName("Get profile of user with empty username, check error message")
  void getProfileForEmptyUsername() {
    requestSpecification =
        RestAssured.given()
            .contentType(APPLICATION_JSON.getDetail())
            .pathParam(USERNAME.getDetail(), "");

    // WHEN
    Response response =
        requestSpecification
            .contentType(APPLICATION_JSON.getDetail())
            .get(PROFILES_USERNAME.getEndpoint());

    // THEN
    MatcherAssert.assertThat(
        "Response page do not contain error message",
        response.asString().contains("The page you were looking for doesn't exist (404)"));
  }


  private static UserResponseDto registerUser(RegistrationRequestUser user) {
    RegistrationRequestUserDto requestBody = new RegistrationRequestUserDto(user);

    requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON.getDetail()).body(requestBody);

    return requestSpecification.post(USERS.getEndpoint()).as(UserResponseDto.class);
  }
}
