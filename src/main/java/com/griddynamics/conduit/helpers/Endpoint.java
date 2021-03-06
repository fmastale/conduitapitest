package com.griddynamics.conduit.helpers;

public enum Endpoint {
  BASE_URI("https://conduit.productionready.io/api"),

  USERS_LOGIN("/users/login"),
  USER("/user"),
  USERS("/users"),

  PROFILES_USERNAME("/profiles/{username}"),
  PROFILES_USERNAME_FOLLOW("/profiles/{username}/follow"),

  ARTICLES("/articles"),
  ARTICLES_LIMIT("/articles?limit={number}"),
  ARTICLES_TAG("/articles?tag={tag}"),
  ARTICLES_SLUG("/articles/{slug}"),
  ARTICLES_FEED("/articles/feed"),
  ARTICLES_SLUG_FAVORITE("/articles/{slug}/favorite"),
  ARTICLES_SLUG_COMMENTS("/articles/{slug}/comments"),
  ARTICLES_SLUG_COMMENTS_ID("/articles/{slug}/comments/{id}"),

  TAGS("/tags");

  private final String endpoint;

  Endpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public String get() {
    return endpoint;
  }
}
