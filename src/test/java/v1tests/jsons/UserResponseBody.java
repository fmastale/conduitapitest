package v1tests.jsons;

public class UserResponseBody {
  public int id;
  public String email;
  public String createdAt;
  public String updatedAt;
  public String username;
  public String bio;
  public String image;
  public String token;

  public UserResponseBody() {
  }

  public UserResponseBody(int id, String email, String createdAt, String updatedAt,
      String username, String bio, String image, String token) {
    this.id = id;
    this.email = email;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.username = username;
    this.bio = bio;
    this.image = image;
    this.token = token;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

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

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
