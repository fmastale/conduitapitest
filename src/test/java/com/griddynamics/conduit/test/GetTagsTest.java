package com.griddynamics.conduit.test;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.jsons.Tags;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("Smoke tests")
@Feature("Get Tags")
public class GetTagsTest {

    @BeforeAll
    static void prepareEnvironment() {
        RestAssured.baseURI = Endpoint.BASE_URI.getEndpoint();
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Get list of tags, check if list contains 20 elements")
    @Test
    @DisplayName("Get tags list, check length")
    void getTagsCheckLength() {
        // GIVEN
        RequestSpecification requestSpecification = RestAssured.given();

        // WHEN
        Tags tagsList = requestSpecification.get("/tags").as(Tags.class);

        // THEN
        MatcherAssert.assertThat("Actual tags list length is different than expected", tagsList.tags.length, Matchers.equalTo(20));
    }
}
