package com.griddynamics.conduit.test;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.helpers.TokenProvider;
import com.griddynamics.conduit.jsons.RegistrationRequestUser;
import com.griddynamics.conduit.jsons.UserRequest;
import com.griddynamics.conduit.jsonsdtos.ProfileDto;
import com.griddynamics.conduit.jsonsdtos.RegistrationRequestUserDto;
import com.griddynamics.conduit.jsonsdtos.UserResponseDto;
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

import static com.griddynamics.conduit.helpers.Endpoint.*;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.*;

@Epic("Smoke tests")
@Feature("Unfollow User")
public class UnfollowUserTest {

    private static String token;
    private static TestDataProvider testDataProvider = new TestDataProvider();
    private static UserRequest registeredUser = testDataProvider.getTestUser();
    private static RequestSpecification requestSpecification;
    private static UserResponseDto user;

    @BeforeAll
    static void prepareEnvironment() {
        RestAssured.baseURI = Endpoint.BASE_URI.getEndpoint();

        TokenProvider tokenProvider = new TokenProvider();
        token = tokenProvider.getTokenForUser(registeredUser);

        // for every new user 'follow' is set to: false
        user = registerUser(testDataProvider.getValidRegistrationUser());
        startFollowingUser(user);
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Stop following valid user, check if 'follow' field is set to 'false'")
    @Test
    @DisplayName("Stop following user, check 'follow' field")
    void unfollowUserCheckFollowField() {
        // GIVEN
        requestSpecification = prepareRequestBody(token, user);

        // WHEN
        ProfileDto profile = getProfileFromApiCall(requestSpecification);

        // THEN
        MatcherAssert.assertThat("'Follow' field should be set to 'false', but was 'true'",
                profile.profile.following, Matchers.equalTo(false));
    }

    private ProfileDto getProfileFromApiCall(RequestSpecification requestSpecification) {
        return requestSpecification.contentType("application/json")
                .delete(PROFILES_USERNAME_FOLLOW.getEndpoint()).as(ProfileDto.class);
    }

    private static UserResponseDto registerUser(RegistrationRequestUser user) {
        RegistrationRequestUserDto requestBody = new RegistrationRequestUserDto(user);

        requestSpecification =
                RestAssured.given().contentType(APPLICATION_JSON.getDetail()).body(requestBody);

        return requestSpecification.post(USERS.getEndpoint()).as(UserResponseDto.class);
    }

    private static RequestSpecification prepareRequestBody(String token, UserResponseDto user) {
        return RestAssured.given().header(AUTHORIZATION.getDetail(), token).pathParam(USERNAME.getDetail(), user.user.username);
    }

    private static ProfileDto followUserApiCall(RequestSpecification requestSpecification) {
        return requestSpecification.contentType("application/json")
                .post(PROFILES_USERNAME_FOLLOW.getEndpoint()).as(ProfileDto.class);
    }

    private static void startFollowingUser(UserResponseDto user) {
        requestSpecification = prepareRequestBody(token, user);
        followUserApiCall(requestSpecification);
    }
}
