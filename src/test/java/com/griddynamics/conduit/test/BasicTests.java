package com.griddynamics.conduit.test;

import static com.griddynamics.conduit.helpers.Endpoint.ARTICLES_SLUG;
import static com.griddynamics.conduit.helpers.Endpoint.PROFILES_USERNAME;
import static com.griddynamics.conduit.helpers.Endpoint.USER;
import static com.griddynamics.conduit.helpers.Endpoint.USERS_LOGIN;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.AUTHORIZATION;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.SLUG;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.USERNAME;
import static com.griddynamics.conduit.helpers.StatusCode.CODE_200;

import com.griddynamics.conduit.jsons.UserRequest;
import com.griddynamics.conduit.jsonsdtos.ProfileDto;
import com.griddynamics.conduit.jsonsdtos.UserRequestDto;
import com.griddynamics.conduit.jsonsdtos.UserResponseDto;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BasicTests extends ApiTest {
  protected Response response;
  protected UserRequest userBody;
  protected ProfileDto responseBody;
  protected UserRequestDto requestBody;
  protected UserResponseDto userResponseDto;
  protected RequestSpecification requestSpecification;

  @Test
  @DisplayName("Authentication - check if user will be logged and then check his ID")
  void logUserAndGetHisId() {
    //GIVEN
    userBody = new UserRequest(TEST_DATA_PROVIDER.getEmail(), TEST_DATA_PROVIDER.getPassword());
    requestBody = new UserRequestDto(userBody);

    requestSpecification = RestAssured.given()
        .contentType(APPLICATION_JSON.getDetail())
        .body(requestBody);

    //WHEN
    response = requestSpecification.post(USERS_LOGIN.getEndpoint());
    userResponseDto = response.as(UserResponseDto.class);

    // THEN
    MatcherAssert.assertThat("Username is different than expected",
        userResponseDto.user.username, Matchers.equalTo(TEST_DATA_PROVIDER.getUsername()));
  }
  
  @Test
  @DisplayName("Get User - get user, then check his bio and response status code")
  void getUserCheckBioAndStatusCode() {
    // GIVEN
    requestSpecification = RestAssured.given()
        .contentType(APPLICATION_JSON.getDetail())
        .header(AUTHORIZATION.getDetail(), TOKEN);

    //WHEN
    response = requestSpecification.get(USER.getEndpoint());
    userResponseDto = response.as(UserResponseDto.class);

    statusCode = response.statusCode();

    //THEN
    MatcherAssert.assertThat("Expected user bio is different",
        TEST_DATA_PROVIDER.getBio(), Matchers.equalTo(userResponseDto.user.bio));

    MatcherAssert.assertThat("Status code different than expected",
        statusCode, Matchers.equalTo(CODE_200.getValue()));
  }

  @Test
  @DisplayName("Get Profile - get user profile and check if username is the same as in query")
  void getProfileCheckUsername() {
    // GIVEN
    requestSpecification = RestAssured.given()
        .contentType(APPLICATION_JSON.getDetail())
        .header(AUTHORIZATION.getDetail(), TOKEN)
        .pathParam(USERNAME.getDetail(), TEST_DATA_PROVIDER.getUsername());

    //WHEN
    response = requestSpecification.get(PROFILES_USERNAME.getEndpoint());
    responseBody = response.as(ProfileDto.class);

    //THEN
    MatcherAssert.assertThat("Username is different than expected",
        responseBody.profile.username, Matchers.equalTo(TEST_DATA_PROVIDER.getUsername()));
  }
  
  @Test
  @DisplayName("Update User - update user bio and check if it was updated")
  void updateUserBioAndCheckIfUpdated() {
    //GIVEN
    userBody = new UserRequest(TEST_DATA_PROVIDER.getUsername(),
        TEST_DATA_PROVIDER.getEmail(), TEST_DATA_PROVIDER
        .getUpdatedBio());
    requestBody = new UserRequestDto(userBody);

    requestSpecification = RestAssured.given()
        .contentType(APPLICATION_JSON.getDetail())
        .header(AUTHORIZATION.getDetail(), TOKEN)
        .body(requestBody);

    //WHEN
    response = requestSpecification.put(USER.getEndpoint());
    userResponseDto = response.as(UserResponseDto.class);

    //THEN
    MatcherAssert.assertThat("Expected user bio is different",
        userResponseDto.user.bio, Matchers.equalTo(TEST_DATA_PROVIDER.getUpdatedBio()));
  }

  @Test
  @DisplayName("Delete Article - remove article")
  void deleteArticleBySlug() {
    //GIVEN
    String slug = "how-to-automate-test-in-restassured-862pmu";

    requestSpecification = RestAssured.given()
        .contentType(APPLICATION_JSON.getDetail())
        .header(AUTHORIZATION.getDetail(), TOKEN)
        .pathParam(SLUG.getDetail(), slug);

    //WHEN
    response = requestSpecification.delete(ARTICLES_SLUG.getEndpoint());
    statusCode = response.statusCode();

    //THEN
    MatcherAssert.assertThat("Status code is different than expected",
        statusCode, Matchers.equalTo(CODE_200.getValue()));
  }
}
