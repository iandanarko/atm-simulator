package test.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;


public class SessionTest {
  @BeforeEach
  public void clearSession() {
    Session.clearSession();
  }

  @Test
  public void getUserEmpty() {
    assertNull(Session.getUser());
  }

  @Test
  public void getUserNotNull() {
    try {
      User user = new User("someone");
      Session.setSession(user);
      assertEquals(user, Session.getUser());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void isNullSessionTestEmpty() {
    Session.clearSession();
    assertTrue(Session.isNullSession());
  }

  @Test
  public void isNullSessionTestNotEmpty() {
    try {
      Session.setSession(new User("someone"));
      assertFalse(Session.isNullSession());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
}
