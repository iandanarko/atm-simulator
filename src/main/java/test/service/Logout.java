package test.service;

import test.entity.Session;
import test.entity.User;
import test.exception.UserNotLoginException;

public class Logout implements ILogout {
  public void logout() throws Exception {
    if (Session.getUser() == null) {
      throw new UserNotLoginException();
    }
    User user = Session.getUser();
    Session.clearSession();
    System.out.printf("Goodbye, %s!\n", user.getName());
  }
}
