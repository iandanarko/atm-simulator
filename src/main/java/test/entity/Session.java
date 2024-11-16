package test.entity;

public class Session {
  private static Session session;
  private User currUser;

  private Session(User user) {
    this.currUser = user;
  }

  public static User getUser() {
    if (session == null) {
      return null;
    }
    return session.currUser;
  }

  public static void setSession(User user) {
    session = new Session(user);
  }

  public static void clearSession() {
    session = null;
  }

  public static boolean isNullSession() {
    return session == null;
  }
}
