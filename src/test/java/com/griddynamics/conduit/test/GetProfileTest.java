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
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GetProfileTest {
  private static UserResponseDto regularUser;
  private static UserResponseDto maxLengthNameUser;
  private static RequestSpecification requestSpecification;

  private ProfileDto userProfile;

  // todo: special chars, space inside

  @BeforeAll
  static void prepareEnvironment() {
    RestAssured.baseURI = Endpoint.BASE_URI.getEndpoint();

    regularUser = registerUser(new TestDataProvider().getValidRegistrationUser());
    maxLengthNameUser = registerUser(new TestDataProvider().getUserWithMaxName());
  }

  @Test
  @DisplayName("Get profile for valid user, check username")
  void getValidUserProfile() {
    // GIVEN
    requestSpecification = prepareRequestSpecification(regularUser.user.username);

    // WHEN
    userProfile = getProfileFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual username is different than expected",
        userProfile.profile.username,
        Matchers.equalTo(regularUser.user.username));
  }

  @Test
  @DisplayName("Get profile for username with max length, check username")
  void getProfileForUserWithMaxUsername() {
    // GIVEN
    requestSpecification = prepareRequestSpecification(maxLengthNameUser.user.username);

    // WHEN
    userProfile = getProfileFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual username is different than expected",
        userProfile.profile.username,
        Matchers.equalTo(maxLengthNameUser.user.username));
  }

  @Test
  @DisplayName("Get profile of user with random and incorrect username, check error message")
  void getProfileForIncorrectUsername() {
    // GIVEN
    requestSpecification =
        prepareRequestSpecification(new TestDataProvider().getRandomIncorrectUsername());

    // WHEN
    GenericError error = getErrorFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Expected and actual error messages are not the same",
        error.error,
        Matchers.equalTo("Not Found"));
  }

  @Test
  @DisplayName("Get profile of user with empty username, check error message")
  void getProfileForEmptyUsername() {
    // GIVEN
    requestSpecification = prepareRequestSpecification("");

    // WHEN
    Response response = getResponseFromApiCall(requestSpecification);

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

  private RequestSpecification prepareRequestSpecification(String username) {
    return RestAssured.given().pathParam(USERNAME.getDetail(), username);
  }

  private ProfileDto getProfileFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.get(PROFILES_USERNAME.getEndpoint()).as(ProfileDto.class);
  }

  private GenericError getErrorFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.get(PROFILES_USERNAME.getEndpoint()).as(GenericError.class);
  }

  private Response getResponseFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification
        .contentType(APPLICATION_JSON.getDetail())
        .get(PROFILES_USERNAME.getEndpoint());
  }
}
