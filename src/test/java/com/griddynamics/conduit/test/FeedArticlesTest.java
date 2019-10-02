package com.griddynamics.conduit.test;

import com.griddynamics.conduit.helpers.Endpoint;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

@Epic("Smoke tests")
@Feature("Feed Articles")
public class FeedArticlesTest {

    @BeforeAll
    static void prepareEnvironment() {
        RestAssured.baseURI = Endpoint.BASE_URI.get();
    }

    // 1. create user follower
    // 2. create user author
    // 3. create article for author
    // 4. follower start following author
    // 5. follower check if got on feed list authors article with given tag

}
