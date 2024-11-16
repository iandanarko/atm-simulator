package test.repository;

import java.util.HashMap;

import test.entity.User;

public class UserRepo implements IUserRepo {
  private HashMap<String, User> db;
  public UserRepo() {
    db = new HashMap<>();
  }

  public User findUserByName(String name) {
    return db.get(name);
  }

  public void insert(User user) {
    db.put(user.getName(), user);
  }
}
