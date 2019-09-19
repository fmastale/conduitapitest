import io.restassured.specification.RequestSpecification;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BasicTest {
  private static String url = "https://conduit.productionready.io/api";
  private static File userJson = new File("user.json");
  private static File updatedUserJson = new File("updatedUser.json");
  private static String TOKEN;

  @BeforeAll
  static void gettingTokenForAllTests() {
    // getting valid token for all tests
    TOKEN =
        given()
            .contentType("application/json")
            .body(userJson)
            .when()
            .post(url + "/users/login")
            .then()
            .statusCode(200)
            .extract()
            .path("user.token");
  }

  // test is using json with user body
  @Test
  @DisplayName("Authentication - log user, check his ID and status code")
  void logUserCheckIdAndStatus() {

    given()
        .contentType("application/json")
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

  // test is using token in header
  @Test
  @DisplayName("Get Current UserResponse - check user email and status code")
  void getUserCheckEmailAndStatus() {

    given()
        .contentType("application/json")
        .header("Authorization", "Token " + TOKEN)
        .when()
        .get(url + "/user")
        .then()
        .body("user.email", IsEqual.equalTo("adam@mail.com"))
        .and()
        .statusCode(200)
        .log()
        .all();
  }

  // test is using parameters in URL
  @Test
  @DisplayName("Get Profile - check username and status code")
  void getProfileCheckUsernameAndStatus() {
    given()
        .contentType("application/json")
        .pathParam("username", "adam1234io")
        .when()
        .get(url + "/profiles/{username}")
        .then()
        .body("profile.username", IsEqual.equalTo("adam1234io"))
        .and()
        .statusCode(200)
        .log()
        .all();
  }

  @Test
  @DisplayName("Update UserResponse Profile - update and check bio, then check the status code ")
  void updateUserProfile() {
    myGiven()
        .body(updatedUserJson)
        .when()
        .put(url + "/user")
        .then()
        .body("user.bio", IsEqual.equalTo("I like to skateboard all day"))
        .and()
        .statusCode(200)
        .log()
        .all();
  }

  @Test
  @DisplayName("Delete Article - remove article")
  void deleteArticleBySlug() {
    String slug = "how-to-automate-test-in-restassured-jyworg";

    myGiven()
        .when()
        .delete(url + "/articles/" + slug)
        .then()
        .assertThat()
        .statusCode(200)
        .log()
        .all();
  }

  private RequestSpecification myGiven() {
    return given().header("Authorization", "Token " + TOKEN).contentType("application/json");
  }
}
