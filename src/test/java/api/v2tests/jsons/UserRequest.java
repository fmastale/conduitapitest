package api.v2tests.jsons;

public class UserRequest {
  public String email;
  public String password;
  public String username;
  public String bio;
  public String image;

  public UserRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public UserRequest(String username, String email, String bio) {
    this.username = username;
    this.email = email;
    this.bio = bio;
  }
}
