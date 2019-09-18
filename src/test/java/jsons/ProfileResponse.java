package jsons;

public class ProfileResponse {
  private String username;
  private String bio;
  private String image;
  private boolean following;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public boolean isFollowing() {
    return following;
  }

  public void setFollowing(boolean following) {
    this.following = following;
  }

  @Override
  public String toString() {
    return "ProfileResponse{" +
        "username='" + username + '\'' +
        ", bio='" + bio + '\'' +
        ", image='" + image + '\'' +
        ", following=" + following +
        '}';
  }
}
