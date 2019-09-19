package v1tests.basictestsv2;

import static io.restassured.RestAssured.given;
import static junit.framework.Assert.assertEquals;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import v1tests.jsons.UserRequestBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import v1tests.wrappers.UserRequestWrapper1;
import v1tests.wrappers.UserResponseWrapper2;

public class MapperTest {
  private static String URL = "https://conduit.productionready.io/api";

  @Test
  @DisplayName("Authentication - log user, check his ID and status code, but with using ObjectMapper")
  void logUserCheckIdAndStatusWithObjectMapper() throws IOException {

    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    UserRequestBody user = new UserRequestBody("adam@mail.com", "adam1234");
    UserRequestWrapper1 userWrapper = new UserRequestWrapper1(user);

    UserResponseWrapper2 response = mapper.readValue(given()
        .contentType("application/json")
        .body(userWrapper)
        .when()
        .post(URL + "/users/login")
        .body()
        .prettyPrint(), UserResponseWrapper2.class);

    assertEquals("Expected and actual not same", "adam1234io", response.userResponseBody.username);
  }
}