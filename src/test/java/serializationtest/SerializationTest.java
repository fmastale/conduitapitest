package serializationtest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SerializationTest {
  private static String url = "https://conduit.productionready.io/api";

  @Test
  @DisplayName("Serialization test")
  void serializationTest() {
    User user = new User();
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
        .all();
  }
}
