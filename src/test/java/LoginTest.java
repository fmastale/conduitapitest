import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTest {
    @Test
    void loginTest() {
        RestAssured.baseURI = "https://conduit.productionready.io/api";
        RequestSpecification request = RestAssured.given();

        JSONObject requestParams = new JSONObject();
        requestParams.put("name", "adam1234io");
        requestParams.put("password", "adam1234");

        JSONObject uberParams = new JSONObject();
        uberParams.put("user", requestParams);

        request.header("Content-Type", "application/json");
        request.body(uberParams);

        Response response = request.post("/users/login");

        String successBody = response.toString();
        System.out.println(successBody);

        int statusCode = response.getStatusCode();
        assertEquals(statusCode, 200);


    }
}
