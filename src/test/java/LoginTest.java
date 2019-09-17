import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LoginTest {
    @Test
    void logUser() {
        String url = "https://conduit.productionready.io/api/users/login";
        File userJson = new File("/home/ferm/IdeaProjects/conduitAnother/user.json");

        given().
                contentType("application/json").
                body(userJson).
                when().
                post(url).
                then().
                statusCode(200);

    }

    @Test
    void logUserAndCheckBody() {
        String url = "https://conduit.productionready.io/api/users/login";
        File userJson = new File("/home/ferm/IdeaProjects/conduitAnother/user.json");

        given().
                contentType("application/json").
                body(userJson).
                when().
                post(url).
                then().
                body("user.id", equalTo(66512)).and().statusCode(200).log();

    }
}
