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
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("Smoke tests")
@Feature("Get Profile")
public class GetProfileTest {
  private static UserResponseDto regularUser;
  private static UserResponseDto maxLengthNameUser;
  private static RequestSpecification requestSpecification;

  private ProfileDto userProfile;

  @BeforeAll
  static void prepareEnvironment() {
    RestAssured.baseURI = Endpoint.BASE_URI.get();

    regularUser = registerUser(new TestDataProvider().getValidRegistrationUser());
    maxLengthNameUser = registerUser(new TestDataProvider().getUserWithMaxName());
  }

  @Severity(SeverityLevel.NORMAL)
  @Description(
      "Get profile of user using valid username in path, check if actual username is same as expected")
  @Test
  @DisplayName("Get profile for valid user, check username")
  void getProfileForValidUsername() {
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

  @Severity(SeverityLevel.NORMAL)
  @Description(
      "Get profile of user using valid username (with max length) in path, check if actual username is same as expected")
  @Test
  @DisplayName("Get profile for username with max length, check username")
  void getProfileForValidNameWithMaxLength() {
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

  @Severity(SeverityLevel.NORMAL)
  @Description(
      "Get profile of user using random and incorrect username in path, check if actual error message is same as expected")
  @Test
  @DisplayName("Get profile of user with random and incorrect username, check error message")
  void cantGetProfileForIncorrectUsername() {
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

  @Severity(SeverityLevel.NORMAL)
  @Description(
      "Get profile of user by using empty username in path, check if actual error message is same as expected")
  @Test
  @DisplayName("Get profile of user with empty username, check error message")
  void cantGetProfileForEmptyUsername() {
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
        RestAssured.given().contentType(APPLICATION_JSON.get()).body(requestBody);

    return requestSpecification.post(USERS.get()).as(UserResponseDto.class);
  }

  private RequestSpecification prepareRequestSpecification(String username) {
    return RestAssured.given().pathParam(USERNAME.get(), username);
  }

  private ProfileDto getProfileFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.get(PROFILES_USERNAME.get()).as(ProfileDto.class);
  }

  private GenericError getErrorFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.get(PROFILES_USERNAME.get()).as(GenericError.class);
  }

  private Response getResponseFromApiCall(RequestSpecification requestSpecification) {
    return requestSpecification.contentType(APPLICATION_JSON.get()).get(PROFILES_USERNAME.get());
  }
}
