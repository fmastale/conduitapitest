package v2tests.utils;

import static v2tests.utils.ApiAddressesUtil.URI;
import static v2tests.utils.ApiAddressesUtil.USERS_LOGIN;
import static v2tests.utils.RequestSpecificationDetails.APPLICATION_JSON;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import v2tests.jsons.UserRequest;
import v2tests.jsonwrappers.UserRequestWrapper;
import v2tests.jsonwrappers.UserResponseWrapper;

public class TestUtils {
  private UserResponseWrapper response;

  public UserResponseWrapper logUser(String email, String password) {
    RestAssured.baseURI = URI;
    UserRequest userRequest = new UserRequest(email, password);
    UserRequestWrapper requestBody = new UserRequestWrapper(userRequest);

    RequestSpecification requestSpecification =
        RestAssured.given().contentType(APPLICATION_JSON).body(requestBody);

    UserResponseWrapper response =
        requestSpecification.post(USERS_LOGIN).as(UserResponseWrapper.class);

    return response;
  }

  public String getToken(String email, String password) {
    this.response = logUser(email, password);
    return response.user.token;
  }
}
