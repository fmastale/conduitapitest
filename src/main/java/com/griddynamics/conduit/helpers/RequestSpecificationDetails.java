package com.griddynamics.conduit.helpers;

public enum RequestSpecificationDetails {
  APPLICATION_JSON("application/json"),
  AUTHORIZATION("Authorization"),
  USERNAME("username"),
  LIMIT_NUMBER("number"),
  TAG("tag"),
  SLUG("slug"),
  ID("id");

  private final String detail;

  RequestSpecificationDetails(String detail) {
    this.detail = detail;
  }

  public String get() {
    return detail;
  }
}
