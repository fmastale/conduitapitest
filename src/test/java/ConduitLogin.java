import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;

public class ConduitLogin {
    @Test
    void conduitLoginTest() {
        String apiPath = "https://conduit.productionready.io/api/profiles/adam1234io";

        get(apiPath).then().statusCode(200);

    }
}
