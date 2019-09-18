package serializationtest;

public class UserWrapper {
  private User user;

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "UserWrapper{" +
        "user=" + user +
        '}';
  }
}
