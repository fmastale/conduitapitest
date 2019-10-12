package com.griddynamics.conduit.helpers;

import static com.griddynamics.conduit.helpers.Endpoint.ARTICLES;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.APPLICATION_JSON;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.AUTHORIZATION;
import static com.griddynamics.conduit.helpers.RequestSpecificationDetails.SLUG;

import com.griddynamics.conduit.jsons.Article;
import com.griddynamics.conduit.jsonsdtos.ArticleDto;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ArticleHelper {
  private final String token;

  public ArticleHelper(String token) {
    this.token = token;
  }

  public Response createArticle(Article article) {
    RequestSpecification requestSpecification =
        RestAssured.given()
            .contentType(APPLICATION_JSON.get())
            .header(AUTHORIZATION.get(), token)
            .body(article);

    Response response = requestSpecification.post(ARTICLES.get());

    checkIfSucceeded(response);

    return response;
  }

  public void removeArticle(String slug, String token) {
    RequestSpecification requestSpecification =
        RestAssured.given().header(AUTHORIZATION.get(), token).pathParam(SLUG.get(), slug);

    Response response = requestSpecification.delete(Endpoint.ARTICLES_SLUG.get());

    checkIfSucceeded(response);
  }

  public String getSlugFromCreatedArticle(String token) {
    Article article = new Article("Title", "Description", "Another body");

    Response response = createArticle(article);
    checkIfSucceeded(response);

    ArticleDto createdArticle = response.as(ArticleDto.class);

    return createdArticle.article.slug;
  }

  public void checkIfSucceeded(Response response) {
    if (response.statusCode() != 200) {
      throw new IllegalStateException("Status code is not 200");
    }
  }
}
