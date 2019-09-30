package com.griddynamics.conduit.helpers;

public enum Endpoint {

  BASE_URI("https://conduit.productionready.io/api"),
  USERS_LOGIN("/users/login"),
  USER("/user"),
  USERS("/users"),
  PROFILES_USERNAME("/profiles/{username}"),
  PROFILES_USERNAME_FOLLOW("/profiles/{username}/follow"),
  ARTICLES_SLUG("/articles/{slug}");

  private final String endpoint;

  Endpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public String getEndpoint() {
    return endpoint;
  }
}
