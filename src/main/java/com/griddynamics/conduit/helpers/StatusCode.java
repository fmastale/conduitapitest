package com.griddynamics.conduit.helpers;

public enum StatusCode {
  _200(200),
  _401(401),
  _422(422),
  _404(404);

  private final int value;

  StatusCode(int value) {
    this.value = value;
  }

  public int get() {
    return value;
  }
}
