package wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jsons.ProfileResponse;

public class ProfileResponseWrapper {
  @JsonProperty("profile")
  private ProfileResponse profileResponse;

  public ProfileResponse getProfileResponse() {
    return profileResponse;
  }

  public void setProfileResponse(ProfileResponse profileResponse) {
    this.profileResponse = profileResponse;
  }

  @Override
  public String toString() {
    return "ProfileResponseWrapper{" +
        "profileResponse=" + profileResponse +
        '}';
  }
}
