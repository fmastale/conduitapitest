package com.griddynamics.conduit.test.utils;

import com.griddynamics.conduit.test.jsons.UserRequest;
import com.griddynamics.conduit.test.jsonswrappers.UserRequestWrapper;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import com.griddynamics.conduit.test.jsonswrappers.UserResponseWrapper;

public class TestUtils {
  private UserResponseWrapper response;

  public UserResponseWrapper logUser(String email, String password) {
    RestAssured.baseURI = ApiAddressesUtil.URI;
    UserRequest userRequest = new UserRequest(email, password);
    UserRequestWrapper requestBody = new UserRequestWrapper(userRequest);

    RequestSpecification requestSpecification =
        RestAssured.given().contentType(RequestSpecificationDetails.APPLICATION_JSON).body(requestBody);

    UserResponseWrapper response =
        requestSpecification.post(ApiAddressesUtil.USERS_LOGIN).as(UserResponseWrapper.class);

    return response;
  }

  public String getToken(String email, String password) {
    this.response = logUser(email, password);
    return response.user.token;
  }
}
