package com.griddynamics.conduit.test;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.helpers.TokenProvider;
import com.griddynamics.conduit.jsons.UserRequest;
import io.restassured.RestAssured;

public class BaseTest {
    protected static String token;
    private static TestDataProvider testDataProvider = new TestDataProvider();
    private static UserRequest user = testDataProvider.getTestUserOne();

    public BaseTest() {
        RestAssured.baseURI = Endpoint.BASE_URI.get();
        token = new TokenProvider().getTokenForUser(user);
    }
}
