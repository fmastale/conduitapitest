package serializationtest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

public class SerializationTest {
  private static String url = "https://conduit.productionready.io/api";

  @Test
  @DisplayName("Serialization test")
  void serializationTest() {
    /*User user = new User();
    user.setEmail("adam@mail.com");
    user.setPassword("adam1234");

    UserWrapper userWrapper = new UserWrapper();
    userWrapper.setUser(user);

    System.out.println(user.getEmail());
    System.out.println(user.getPassword());

    given()
        .contentType("application/json; charset=UTF-8")
        .body(userWrapper)
        .when()
        .post(url + "/users/login")
        .then()
        .body("user.id", equalTo(66512))
        .and()
        .statusCode(200)
        .log()
        .all();*/
  }

  @Test
  @DisplayName("Deserialize json")
  void deserializeTest() {
    File userJson = new File("user.json");
    AutenticationUser autenticationUser= new AutenticationUser();
    autenticationUser.setEmail("adam@mail.com");
    autenticationUser.setPassword("adam1234");

    AutenticationUserWrapper autenticationUserWrapper = new AutenticationUserWrapper();
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
