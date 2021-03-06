package com.griddynamics.conduit.test.articles;

import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;

import com.griddynamics.conduit.helpers.ArticleHelper;
import com.griddynamics.conduit.helpers.Endpoint;
import com.griddynamics.conduit.helpers.RequestSpecificationDetails;
import com.griddynamics.conduit.jsons.Article;
import com.griddynamics.conduit.jsonsdtos.ArticleDto;
import com.griddynamics.conduit.test.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Epic("Smoke tests")
@Feature("Update Article")
public class UpdateArticleTest extends BaseTest {

  private String slug;
  private ArticleHelper articleHelper = new ArticleHelper(token);

  @BeforeEach
  void prepareSlug() {
    slug = articleHelper.getSlugFromCreatedArticle();
  }

  @AfterEach
  void removeArticle() {
    articleHelper.removeArticle(slug);
  }

  @Severity(SeverityLevel.NORMAL)
  @Description("Update article, check if updated bio is different than the old one")
  @Test
  @DisplayName("Update article, check if bio is updated")
  void updateArticleCheckBio() {
    // GIVEN
    Article updatedArticle = new Article("Title", "Updated description", "Body");

    RequestSpecification requestSpecification =
        prepareRequestSpecification(updatedArticle, token, slug);

    // WHEN
    ArticleDto responseArticle =
        requestSpecification.put(Endpoint.ARTICLES_SLUG.get()).as(ArticleDto.class);

    // THEN
    MatcherAssert.assertThat(
        "Actual description is different than expected",
        responseArticle.article.description,
        Matchers.equalTo(updatedArticle.description));
  }

  private RequestSpecification prepareRequestSpecification(
      Article article, String token, String slug) {
    return RestAssured.given()
        .contentType(APPLICATION_JSON.get())
        .header(RequestSpecificationDetails.AUTHORIZATION.get(), token)
        .pathParam(RequestSpecificationDetails.SLUG.get(), slug)
        .body(article);
  }
}
