package serializationtest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import jsons.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wrappers.UserRequestWrapper;
import wrappers.UserResponseWrapper;

public class SerializationTest {
  private static String url = "https://conduit.productionready.io/api";

  @Test
  @DisplayName("Serialization test")
  void serializationTest() {
    File userJson = new File("user.json");

    given()
        .contentType("application/json; charset=UTF-8")
        .body(userJson)
        .when()
        .post(url + "/users/login")
        .then()
        .body("user.id", equalTo(66512))
        .and()
        .statusCode(200)
        .log()
        .all();
  }

  @Test
  @DisplayName("Deserialize json")
  void deserializeTest() {
    UserRequest userRequest = new UserRequest();
    userRequest.setEmail("adam@mail.com");
    userRequest.setPassword("adam1234");
    UserRequestWrapper requestWrapper = new UserRequestWrapper();
    requestWrapper.setUserRequest(userRequest);


    RestAssured.baseURI = url;
    RequestSpecification request = RestAssured.given();

    Response response = request
        .contentType("application/json; charset=UTF-8")
        .body(requestWrapper)
        .post(url + "/users/login");

    System.out.println(response.asString());

    UserResponseWrapper responseWrapper = response.as(UserResponseWrapper.class);

    System.out.println(responseWrapper.getUserResponse().getBio());
  }
}
