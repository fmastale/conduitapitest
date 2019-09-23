package com.griddynamics.conduit.test;

import com.griddynamics.conduit.helpers.TestDataProvider;
import com.griddynamics.conduit.jsonsdtos.RegistrationRequestUserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RegistrationTest {
  TestDataProvider testDataProvider;

  @Test
  @DisplayName("Registration - register user with valid data")
  void registerUserWithValidData() {
    //GIVEN
    RegistrationRequestUserDto requestBody = new RegistrationRequestUserDto(testDataProvider.getRegistrationUser());

    //WHEN

    //THEN

  }

}
