import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SampleTest {
    /*@Test
    void conduitLoginTest() {
        RestAssured.baseURI = "https://conduit.productionready.io/api";
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.request(Method.GET);

        JsonPath jsonPathEvaluator = response.jsonPath();
        Map<String, String> values = jsonPathEvaluator.get("profile");

        String bio = values.get("bio");

        assertEquals(bio, "I like cookies", "messages not same");
    }*/
}
