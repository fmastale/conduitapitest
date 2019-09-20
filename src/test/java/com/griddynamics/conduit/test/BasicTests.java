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
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.griddynamics.conduit.jsons.UserRequest;
import com.griddynamics.conduit.jsonsdtos.ProfileDto;
import com.griddynamics.conduit.jsonsdtos.UserRequestDto;
import com.griddynamics.conduit.jsonsdtos.UserResponseDto;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
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
        .contentType(APPLICATION_JSON)
        .body(requestBody);

    //WHEN
    response = requestSpecification.post(USERS_LOGIN);
    userResponseDto = response.as(UserResponseDto.class);

    // THEN
    assertEquals(TEST_DATA_PROVIDER.getUsername(), userResponseDto.user.username,
        "Username is different than expected");
  }
  
  @Test
  @DisplayName("Get User - get user, then check his bio and response status code")
  void getUserCheckBioAndStatusCode() {
    // GIVEN
    requestSpecification = RestAssured.given()
        .contentType(APPLICATION_JSON)
        .header(AUTHORIZATION, TOKEN);

    //WHEN
    response = requestSpecification.get(USER);
    userResponseDto = response.as(UserResponseDto.class);

    statusCode = response.statusCode();

    //THEN
    assertEquals(TEST_DATA_PROVIDER.getBio(), userResponseDto.user.bio,
        "Expected user bio is different");
    Assertions.assertEquals(CODE_200, statusCode,
        "Status code different than expected");
  }

  @Test
  @DisplayName("Get Profile - get user profile and check if username is the same as in query")
  void getProfileCheckUsername() {
    // GIVEN
    requestSpecification = RestAssured.given()
        .contentType(APPLICATION_JSON)
        .header(AUTHORIZATION, TOKEN)
        .pathParam(USERNAME, TEST_DATA_PROVIDER.getUsername());

    //WHEN
    response = requestSpecification.get(PROFILES_USERNAME);
    responseBody = response.as(ProfileDto.class);

    //THEN
    Assertions.assertEquals(TEST_DATA_PROVIDER.getUsername(), responseBody.profile.username,
        "Username is different than expected");
  }
  
  @Test
  @DisplayName("Update User - update user bio and check if it was updated")
  void updateUserBioAndCheckIfUpdated() {
    //GIVEN
    userBody = new UserRequest(TEST_DATA_PROVIDER.getUsername(), TEST_DATA_PROVIDER.getEmail(), TEST_DATA_PROVIDER
        .getUpdatedBio());
    requestBody = new UserRequestDto(userBody);

    requestSpecification = RestAssured.given()
        .contentType(APPLICATION_JSON)
        .header(AUTHORIZATION, TOKEN)
        .body(requestBody);

    //WHEN
    response = requestSpecification.put(USER);
    userResponseDto = response.as(UserResponseDto.class);

    //THEN
    assertEquals(TEST_DATA_PROVIDER.getUpdatedBio(), userResponseDto.user.bio,
        "Expected user bio is different");
  }

  @Test
  @DisplayName("Delete Article - remove article")
  void deleteArticleBySlug() {
    //GIVEN
    String slug = "how-to-automate-test-in-restassured-shfi00";

    requestSpecification = RestAssured.given()
        .contentType(APPLICATION_JSON)
        .header(AUTHORIZATION, TOKEN)
        .pathParam(SLUG, slug);

    //WHEN
    response = requestSpecification.delete(ARTICLES_SLUG);
    statusCode = response.statusCode();

    //THEN
    assertEquals(CODE_200, statusCode, "Status code is different than expected");

  }
}
