package serializationtest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    File userJson = new File("user.json");
    AuthenticationUser autenticationUser= new AuthenticationUser();
    autenticationUser.setEmail("adam@mail.com");
    autenticationUser.setPassword("adam1234");

    AuthenticationUserWrapper autenticationUserWrapper = new AuthenticationUserWrapper();
    autenticationUserWrapper.setAutenticationUser(autenticationUser);

    RestAssured.baseURI = url;
    RequestSpecification request = RestAssured.given();

    Response response = request
        .contentType("application/json; charset=UTF-8")
        .body(autenticationUserWrapper)
        .post(url + "/users/login");

    System.out.println(response.asString());

    UserWrapper urw = response.as(UserWrapper.class);

    System.out.println(urw.getUser().getBio());
  }
}
