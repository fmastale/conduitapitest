package v2tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static v2tests.testutils.ApiAddresses.usersLogin;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import v2tests.jsons.UserRequest;
import v2tests.jsonwrappers.UserRequestWrapper;
import v2tests.jsonwrappers.UserResponseWrapper;
import v2tests.testutils.TestUtils;

public class BasicFuncTest {
  //todo: move uri to properties file
  private static final String URI = "https://conduit.productionready.io/api";
  private static String TOKEN;
  private static TestUtils utils = new TestUtils();

  @BeforeAll
  @DisplayName("Get token for all tests")
  static void getToken() {
    TOKEN = utils.getToken();
  }

  @Test
  @DisplayName("Authentication - log user, check his ID and status code")
  void logUserAndCheckId() {
    //GIVEN
    RestAssured.baseURI = URI;
    UserRequest userRequest = new UserRequest("adam@mail.com", "adam1234");
    UserRequestWrapper requestBody = new UserRequestWrapper(userRequest);

    RequestSpecification requestSpecification =
        RestAssured.given().contentType("application/json").body(requestBody);

    //WHEN
    UserResponseWrapper response =
        requestSpecification.post("/users/login").as(UserResponseWrapper.class);

    // THEN
    assertEquals("adam1234io", response.user.username,"Expected and actual should be equal");
  }

  @Test
  @DisplayName("Authentication test - login user with valid email and password")
  void loginUserWithValidCredentials() {
    //GIVEN
    String email = "adam@mail.com";
    String password = "adam1234";

    //WHEN
    UserResponseWrapper response = utils.loginUser(email, password);

    //THEN
    assertEquals("adam1234io", response.user.username,
        "Expected and actual usernames are not the same");
  }


  @Test
  @DisplayName("Authentication - log user, check his ID and status code v.2")
  void logUserAndCheckId2() {
    //GIVEN
    RestAssured.baseURI = URI;
    String email = "adam@mail.com";
    String password = "adam1234";

    UserRequestWrapper requestBody = getUserRequestWrapper(email, password);
    RequestSpecification requestSpecification = getRequestSpecification(requestBody);

    //WHEN
    UserResponseWrapper response = getUserResponseWrapper(requestSpecification);

    // THEN
    assertEquals("adam1234io", response.user.username,"Expected and actual should be equal");
  }

  private UserResponseWrapper getUserResponseWrapper(RequestSpecification requestSpecification) {
    return requestSpecification.post(usersLogin).as(UserResponseWrapper.class);
  }

  private RequestSpecification getRequestSpecification(UserRequestWrapper requestBody) {
    return RestAssured.given().contentType("application/json").body(requestBody);
  }

  private UserRequestWrapper getUserRequestWrapper(String email, String password) {
    UserRequest userRequest = new UserRequest(email, password);
    return new UserRequestWrapper(userRequest);
  }


}