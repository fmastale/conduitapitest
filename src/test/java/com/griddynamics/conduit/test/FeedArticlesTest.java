package com.griddynamics.conduit.test;

import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.jsons.Article;
import com.griddynamics.conduit.jsons.RegistrationRequestUser;
import com.griddynamics.conduit.jsonsdtos.ArticleDto;
import com.griddynamics.conduit.jsonsdtos.ArticlesListDto;
import com.griddynamics.conduit.jsonsdtos.ProfileDto;
import com.griddynamics.conduit.jsonsdtos.RegistrationRequestUserDto;
import com.griddynamics.conduit.jsonsdtos.UserResponseDto;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.griddynamics.conduit.helpers.Endpoint.*;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.*;

@Epic("Smoke tests")
@Feature("Feed Articles")
public class FeedArticlesTest {
    private static UserResponseDto author;
    private static UserResponseDto follower;
    private static ArticleDto article;

    @BeforeAll
    static void prepareEnvironment() {
        RestAssured.baseURI = Endpoint.BASE_URI.get();

        author = registerUser(new TestDataProvider().getValidRegistrationUser());
        article = createArticle(author);

        follower = registerUser(new TestDataProvider().getValidRegistrationUser());
        startFollowingAuthor(follower, author);
    }

    @Test
    @DisplayName("Check feed")
    void checkFeed() {
        // GIVEN
        RequestSpecification requestSpecification = RestAssured.given()
                .contentType(APPLICATION_JSON.getDetails())
                .header(AUTHORIZATION.getDetails(), follower.user.token);

        // WHEN
        ArticlesListDto articles = requestSpecification.get("/articles/feed").as(ArticlesListDto.class);
        System.out.println(articles.articles);
        // THEN
        MatcherAssert.assertThat("", articles.articles.contains(article), Matchers.equalTo(true));
    }


    private static void startFollowingAuthor(UserResponseDto follower, UserResponseDto author) {
        RequestSpecification requestSpecification = RestAssured.given()
                .header(AUTHORIZATION.getDetails(), follower.user.token)
                .pathParam(USERNAME.getDetails(), author.user.username);

        ProfileDto profileDto = requestSpecification
                .contentType("application/json")
                .post(PROFILES_USERNAME_FOLLOW.get())
                .as(ProfileDto.class);
    }

    private static ArticleDto createArticle(UserResponseDto author) {

        RequestSpecification requestSpecification = RestAssured.given()
                .contentType(APPLICATION_JSON.getDetails())
                .header(AUTHORIZATION.getDetails(), author.user.token)
                .body(new Article("This is article title", "This is description", "This is body"));

        Response response =  requestSpecification.post(ARTICLES.get());
        System.out.println(response.statusCode());
        System.out.println(response.prettyPrint());
        return response.as(ArticleDto.class);
    }

    private static UserResponseDto registerUser(RegistrationRequestUser author) {
        RegistrationRequestUserDto requestBody = new RegistrationRequestUserDto(author);
        RequestSpecification requestSpecification =
                RestAssured.given().contentType(APPLICATION_JSON.getDetails()).body(requestBody);

        return requestSpecification.post(USERS.get()).as(UserResponseDto.class);
    }


    // 5. follower check if got on feed list authors article with given tag

}
