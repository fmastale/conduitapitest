import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BasicTest {
    private static String url = "https://conduit.productionready.io/api/";
    private static File userJson = new File("user.json");
    private static String TOKEN;

    @BeforeAll
    static void gettingTokenForAllTests() {
        //getting valid token for all tests
        TOKEN =
                given().
                        contentType("application/json").
                        body(userJson).
                when().
                        post(url + "users/login").
                then().
                        body("user.id", equalTo(66512)).
                and().
                        statusCode(200).
                        extract().
                        path("user.token");
    }

    @Test
    @DisplayName("Authentication - log user, check his ID and status code")
    void logUserCheckIdAndCode() {

        given().
                contentType("application/json").
                body(userJson).
        when().
                post(url + "users/login").
        then().
                body("user.id", equalTo(66512)).
        and().
                statusCode(200).
                log().
                all();
    }

    @Test
    @DisplayName("Get Current User - ")
    void gettingCurrentUser() {

        given().
                contentType("application/json").
                header("Authorization","Token " + TOKEN).
        when().
                get(url + "user").
        then().
                body("user.email", IsEqual.equalTo("adam@mail.com")).
        and().
                statusCode(200).
                log().
                all();

    }

}
