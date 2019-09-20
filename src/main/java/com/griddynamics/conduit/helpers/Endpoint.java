package com.griddynamics.conduit.helpers;

public enum Endpoint {

  BASE_URI("https://conduit.productionready.io/api"),
  USERS_LOGIN("/users/login"),
  USER("/user"),
  PROFILES_USERNAME("/profiles/{username}"),
  ARTICLES_SLUG("/articles/{slug}");

  private final String endpoint;

  Endpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public String getEndpoint() {
    return endpoint;
  }
}
