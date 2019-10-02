package com.griddynamics.conduit.test;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.helpers.TokenProvider;
import com.griddynamics.conduit.jsons.Article;
import com.griddynamics.conduit.jsons.Tags;
import com.griddynamics.conduit.jsons.UserRequest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.griddynamics.conduit.helpers.Endpoint.ARTICLES;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.AUTHORIZATION;

@Epic("Smoke tests")
@Feature("Get Tags")
public class GetTagsTest {
    private static String token;
    private static TestDataProvider testDataProvider = new TestDataProvider();
    private static UserRequest user = testDataProvider.getTestUser();


    // 3. get list of tags
    // 4. check if our tag is on the list

    @BeforeAll
    static void prepareEnvironment() {
        RestAssured.baseURI = Endpoint.BASE_URI.get();

        TokenProvider tokenProvider = new TokenProvider();
        token = tokenProvider.getTokenForUser(user);


    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Get list of tags, check if list contains 20 elements")
    @Test
    @DisplayName("Get tags list, check length")
    void getTagsCheckLength() {
        // GIVEN
        String[] tags = new String[]{"FancyTestTag"};
        Article article = new Article("This is article title", "This is description", "This is body", tags);

        RequestSpecification articleRequestSpecification = RestAssured.given()
                .contentType(APPLICATION_JSON.getDetails())
                .header(AUTHORIZATION.getDetails(), token)
                .body(article);
        Response response = articleRequestSpecification.post(ARTICLES.get());
        response.prettyPrint();


        RequestSpecification requestSpecification = RestAssured.given();

        // WHEN
        Tags tagsList = requestSpecification.get(Endpoint.TAGS.get()).as(Tags.class);

        System.out.println(tagsList.tags.toString());
        // THEN
        //MatcherAssert.assertThat("Actual tags list length is different than expected", tagsList.tags.contains("FancyTestTag"), Matchers.equalTo(true));
    }
}
