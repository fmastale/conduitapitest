package api.v2tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static api.v2tests.utils.ApiAddressesUtil.PROFILES_USERNAME;
import static api.v2tests.utils.ApiAddressesUtil.URI;
import static api.v2tests.utils.ApiAddressesUtil.USER;
import static api.v2tests.utils.ApiAddressesUtil.USERS_LOGIN;

import api.v2tests.jsons.UserRequest;
import api.v2tests.jsonswrappers.ProfileWrapper;
import api.v2tests.jsonswrappers.UserRequestWrapper;
import api.v2tests.utils.RequestSpecificationDetails;
import api.v2tests.utils.StatusCodes;
import api.v2tests.utils.TestDataProvider;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import api.v2tests.jsonswrappers.UserResponseWrapper;
import api.v2tests.utils.TestUtils;

public class BaseNewTests {
  private static String TOKEN;
  private static TestDataProvider DATA = new TestDataProvider();

  @BeforeAll
  @DisplayName("Environment setup - getting valid token and setting base URI")
  static void envSetup() {
    RestAssured.baseURI = URI;

    TestUtils testUtil = new TestUtils();
    TOKEN = "Token " + testUtil.getToken(DATA.getEmail(), DATA.getPassword());
  }

  @Test
  @DisplayName("Authentication - log user, check his ID")
  void logUser() {
    //GIVEN
    UserRequest userBody = new UserRequest(DATA.getEmail(), DATA.getPassword());
    UserRequestWrapper requestBody = new UserRequestWrapper(userBody);

    RequestSpecification requestSpecification =
        RestAssured.given()
        .contentType(RequestSpecificationDetails.APPLICATION_JSON)
        .body(requestBody);

    //WHEN
    Response response = requestSpecification.post(USERS_LOGIN);
    UserResponseWrapper responseBody= response.as(UserResponseWrapper.class);

    // THEN
    assertEquals(DATA.getUsername(), responseBody.user.username,
        "Username is different than expected");
  }
  
  @Test
  @DisplayName("Get User - check bio and status code")
  void getUserCheckBioAndStatusCode() {
    // GIVEN
    RequestSpecification requestSpecification =
        RestAssured.given()
        .contentType(RequestSpecificationDetails.APPLICATION_JSON)
        .header(RequestSpecificationDetails.AUTHORIZATION, TOKEN);

    //WHEN
    Response response = requestSpecification.get(USER);
    UserResponseWrapper responseBody = response.as(UserResponseWrapper.class);

    int statusCode = response.statusCode();

    //THEN
    assertEquals(DATA.getBio(), responseBody.user.bio,
        "Expected user bio is different");
    Assertions.assertEquals(StatusCodes.CODE_200, statusCode,
        "Status code different than expected");
  }

  @Test
  @DisplayName("Get Profile")
  void getProfileCheckUsername() {
    // GIVEN
    RequestSpecification requestSpecification =
        RestAssured.given()
        .contentType(RequestSpecificationDetails.APPLICATION_JSON)
        .header(RequestSpecificationDetails.AUTHORIZATION, TOKEN)
        .pathParam(RequestSpecificationDetails.USERNAME, DATA.getUsername());

    //WHEN
    Response response = requestSpecification.get(PROFILES_USERNAME);
    ProfileWrapper responseBody = response.as(ProfileWrapper.class);

    //THEN
    Assertions.assertEquals(DATA.getUsername(), responseBody.profile.username,
        "Username is different than expected");
  }
  
  @Test
  @DisplayName("Update User")
  void updateUser() {
    //GIVEN
    UserRequest userBody = new UserRequest(DATA.getUsername(), DATA.getEmail(), DATA.getUpdatedBio());
    UserRequestWrapper requestBody = new UserRequestWrapper(userBody);

    RequestSpecification requestSpecification =
        RestAssured.given()
        .contentType(RequestSpecificationDetails.APPLICATION_JSON)
        .header(RequestSpecificationDetails.AUTHORIZATION, TOKEN)
        .body(requestBody);

    //WHEN
    Response response = requestSpecification.put(USER);
    UserResponseWrapper responseBody = response.as(UserResponseWrapper.class);

    //THEN
    assertEquals(DATA.getUpdatedBio(), responseBody.user.bio,
        "Expected user bio is different");
  }
}
