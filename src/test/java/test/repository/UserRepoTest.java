package test.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import test.entity.User;

public class UserRepoTest {
  private User user;
  private UserRepo repo;
  @BeforeEach
  void init() {
    try {
      user = new User("someone");
      repo = new UserRepo();
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
  
  @Test
  public void findUserByNameFoundSuccessTest() {
    repo.insert(user);
    User result = repo.findUserByName(user.getName());
    assertEquals(user, result);
  }

  @Test
  public void findUserByNameNotFoundTest() {
    User result = repo.findUserByName(user.getName());
    assertNull(result);
  }
}
