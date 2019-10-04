package com.griddynamics.conduit.test;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.StatusCode;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.helpers.TokenProvider;
import com.griddynamics.conduit.jsons.GenericError;
import com.griddynamics.conduit.jsons.RegistrationRequestUser;
import com.griddynamics.conduit.jsons.UserRequest;
import com.griddynamics.conduit.jsonsdtos.ProfileDto;
import com.griddynamics.conduit.jsonsdtos.RegistrationRequestUserDto;
import com.griddynamics.conduit.jsonsdtos.UserResponseDto;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.griddynamics.conduit.helpers.Endpoint.PROFILES_USERNAME_FOLLOW;
import static com.griddynamics.conduit.helpers.Endpoint.USERS;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.*;


@Epic("Smoke tests")
@Feature("Follow User")
public class FollowUserTest {
  private static String token;
  private static TestDataProvider testDataProvider = new TestDataProvider();
  private static UserRequest registeredUser = testDataProvider.getTestUserOne();
  private static RequestSpecification requestSpecification;
  private static UserResponseDto userToFollow;

  @BeforeAll
  static void prepareEnvironment() {
    RestAssured.baseURI = Endpoint.BASE_URI.get();

    TokenProvider tokenProvider = new TokenProvider();
    token = tokenProvider.getTokenForUser(registeredUser);

    // for every new user 'follow' is set to: false
    userToFollow = registerUser(testDataProvider.getValidRegistrationUser());
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Start following valid user, check if 'follow' field is set to 'true'")
  @Test
  @DisplayName("Follow valid user, check 'follow' field")
  void followValidUserGetFollowingTrue() {
    // GIVEN
    requestSpecification = prepareRequestBody(token, userToFollow.user.username);

    // WHEN
    ProfileDto response = getProfileFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "'Follow' field should be set to 'true', but was 'false'",
        response.profile.following,
        Matchers.equalTo(true));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Try to start following non existing user, check error message")
  @Test
  @DisplayName("Follow non existing user, check error message")
  void followNonExistingUserGetFollowingFalse() {
    // GIVEN
    requestSpecification = prepareRequestBody(token, testDataProvider.getRandomIncorrectUsername());

    // WHEN
    GenericError responseError = getErrorFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual error message is different than expected",
        responseError.error,
        Matchers.equalTo("Not Found"));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description(
      "Try to start following existing user without being authenticated, check status code")
  @Test
  @DisplayName("Follow user without being authenticated, check status code")
  void followValidUserWithoutBeingAuthenticated() {
    // GIVEN
    requestSpecification = prepareRequestBody(testDataProvider.getRandomIncorrectUsername());

    // WHEN
    int statusCode = getStatusCodeFromApiCall(requestSpecification);

    // THEN
    MatcherAssert.assertThat(
        "Actual status code is different than expected",
        statusCode,
        Matchers.equalTo(StatusCode._401.get()));
  }

  private static UserResponseDto registerUser(RegistrationRequestUser user) {
    RegistrationRequestUserDto requestBody = new RegistrationRequestUserDto(user);

    requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON.get()).body(requestBody);

    return requestSpecification.post(USERS.get()).as(UserResponseDto.class);
  }

  private RequestSpecification prepareRequestBody(String token, String username) {
    return RestAssured.given()
        .header(AUTHORIZATION.get(), token)
        .pathParam(USERNAME.get(), username);
  }

  private RequestSpecification prepareRequestBody(String username) {
    return RestAssured.given().pathParam(USERNAME.get(), username);
  }

  private ProfileDto getProfileFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification
        .contentType("application/json")
        .post(PROFILES_USERNAME_FOLLOW.get())
        .as(ProfileDto.class);
  }

  private GenericError getErrorFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification
        .contentType("application/json")
        .post(PROFILES_USERNAME_FOLLOW.get())
        .as(GenericError.class);
  }

  private int getStatusCodeFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification
        .contentType("application/json")
        .post(PROFILES_USERNAME_FOLLOW.get())
        .statusCode();
  }
}