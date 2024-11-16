package test.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import test.entity.Session;
import test.entity.User;
import test.exception.UserNotLoginException;

public class LogoutTest {
  private ILogout service;
  private User user;

  @BeforeEach
  void init() {
    try {
      user = new User("someone");
      service = new Logout();
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @AfterEach
  void post() {
    Session.clearSession();
  }

  @Test
  public void logoutFailedTest() {
    assertThrowsExactly(UserNotLoginException.class, () -> {service.logout();});
  }

  @Test
  public void logoutSuccessTest() {
    Session.setSession(this.user);
    try {
      service.logout();
    } catch (Exception e) {
      fail(e.getMessage());
    }
    assertNull(Session.getUser());
  }
}
