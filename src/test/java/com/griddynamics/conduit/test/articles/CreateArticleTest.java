package com.griddynamics.conduit.test.articles;

import com.griddynamics.conduit.helpers.ArticleHelper;
import com.griddynamics.conduit.helpers.StatusCode;
import com.griddynamics.conduit.jsons.Article;
import com.griddynamics.conduit.jsonsdtos.ArticleDto;
import com.griddynamics.conduit.test.BaseTest;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.griddynamics.conduit.helpers.Endpoint.ARTICLES;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.AUTHORIZATION;

@Epic("Smoke tests")
@Feature("Create Articles Test")
public class CreateArticleTest extends BaseTest {

  private String slug;
  private ArticleHelper articleHelper = new ArticleHelper();

  @AfterEach
  void cleanup() {
    articleHelper.removeArticle(slug, token);
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Create article, check if status code is 200")
  @Test
  @DisplayName("Create article, check status code")
  void createArticle() {
    // GIVEN
    Article article = prepareArticle();

    RequestSpecification requestSpecification = prepareRequestSpecification(article, token);

    // WHEN
    Response response = requestSpecification.post(ARTICLES.get());
    slug = response.as(ArticleDto.class).article.slug;

    // THEN
    MatcherAssert.assertThat(
        "Actual status code is different than expected",
        response.getStatusCode(),
        Matchers.equalTo(StatusCode._200.get()));
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Create article with additional field, check if status code is 200")
  @Test
  @DisplayName("Create article with additional field, check status code")
  void createArticleWithAdditionalFieldCheckStatus() {
    // GIVEN
    String[] tags = {"tag1", "tag2"};
    Article article = prepareArticle(tags);

    RequestSpecification requestSpecification = prepareRequestSpecification(article, token);

    // WHEN
    Response response = requestSpecification.post(ARTICLES.get());
    slug = response.as(ArticleDto.class).article.slug;

    // THEN
    MatcherAssert.assertThat(
        "Actual status code is different than expected",
        response.getStatusCode(),
        Matchers.equalTo(StatusCode._200.get()));
  }

  private RequestSpecification prepareRequestSpecification(Article article, String token) {
    return RestAssured.given()
        .contentType(APPLICATION_JSON.get())
        .header(AUTHORIZATION.get(), token)
        .body(article);
  }

  private Article prepareArticle(String[] tags) {
    return new Article("This is article title", "This is description", "This is body", tags);
  }

  private Article prepareArticle() {
    return new Article("This is article title", "This is description", "This is body");
  }
}
