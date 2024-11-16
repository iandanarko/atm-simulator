package test.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.Test;

import test.exception.EmptyNameException;

public class UserTest {
  @Test
  public void InitUserFailedNullNameTest() {
      assertThrowsExactly(EmptyNameException.class, () -> {new User(null); });
  }

  @Test
  public void InitUserFailedEmptyNameTest() {
    assertThrowsExactly(EmptyNameException.class, () -> {new User(""); });
  }

  @Test
  public void getNameTest() {
    try {
      String expected = "someone";
      User user = new User(expected);
      assertEquals(expected, user.getName());    
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
}
