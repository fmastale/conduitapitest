package v2tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static v2tests.testutils.ApiAddresses.profilesUsername;
import static v2tests.testutils.ApiAddresses.user;
import static v2tests.testutils.ApiAddresses.usersLogin;
import static v2tests.testutils.StatusCode.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import v2tests.jsons.Profile;
import v2tests.jsons.UserRequest;
import v2tests.jsons.UserResponse;
import v2tests.jsonwrappers.ProfileWrapper;
import v2tests.jsonwrappers.UserRequestWrapper;
import v2tests.jsonwrappers.UserResponseWrapper;
import v2tests.testutils.TestUtils;

public class BaseNewTests {
  private static final String URI = "https://conduit.productionready.io/api";
  private static String TOKEN;
  private static TestUtils utils = new TestUtils();
  private String email = "adam@mail.com";
  private String password = "adam1234";
  private String username = "adam1234io";
  private String bio = "I like to eat cookies";
  private String updatedBio = "I like to ride on skateboard";
  private String parameter = "username";
  private String applicationJson = "application/json";
  private String authorization = "Authorization";


  @BeforeAll
  @DisplayName("Environment setup - getting valid token and setting base URI")
  static void envSetup() {
    TOKEN = "Token " + utils.getToken();
    RestAssured.baseURI = URI;
  }

  @Test
  @DisplayName("Authentication - log user, check his ID and status code")
  void logUserAndCheckId() {
    //GIVEN
    UserRequest userRequest = new UserRequest(email, password);
    UserRequestWrapper requestBody = new UserRequestWrapper(userRequest);

    RequestSpecification requestSpecification =
        RestAssured.given().contentType(applicationJson).body(requestBody);

    //WHEN
    Response response = requestSpecification.post(usersLogin);

    // THEN
    UserResponseWrapper responseBody= response.as(UserResponseWrapper.class);
    assertEquals(username, responseBody.user.username,"Expected and actual should be equal");
  }
  
  @Test
  @DisplayName("Get User - check bio and status code")
  void getUserCheckBioAndStatusCode() {
    // GIVEN
    RequestSpecification requestSpecification =
        RestAssured.given()
            .contentType(applicationJson)
            .header(authorization, TOKEN);
    //WHEN
    Response response = requestSpecification.get(user);
    UserResponseWrapper userResponseWrapper = response.as(UserResponseWrapper.class);

    int statusCode = response.statusCode();
    //THEN

    assertEquals(bio, userResponseWrapper.user.bio,
        "Messages not equal");
    assertEquals(code200, statusCode, "Status codes not equal");
  }

  @Test
  @DisplayName("Get Profile")
  void getProfileCheckUsername() {
    // GIVEN
    RequestSpecification requestSpecification =
        RestAssured.given()
        .contentType(applicationJson)
        .header(authorization, TOKEN)
        .pathParam(parameter, username);

    //WHEN
    Response response = requestSpecification.get(profilesUsername);
    ProfileWrapper profileWrapper = response.as(ProfileWrapper.class);

    //THEN
    assertEquals(username, profileWrapper.profile.username, "Username not the same");
  }
  
  @Test
  @DisplayName("Update User")
  void updateUser() {
    //GIVEN
    UserRequest requestBody = new UserRequest(username, email, updatedBio);
    UserRequestWrapper requestWrapper = new UserRequestWrapper(requestBody);

    RequestSpecification requestSpecification =
        RestAssured.given()
        .contentType(applicationJson)
        .header(authorization, TOKEN)
        .body(requestWrapper);

    //WHEN
    Response response = requestSpecification.put(user);
    UserResponseWrapper responseWrapper = response.as(UserResponseWrapper.class);

    //THEN
    assertEquals(updatedBio, responseWrapper.user.bio, "Expected and actual not the same");
  }
}
