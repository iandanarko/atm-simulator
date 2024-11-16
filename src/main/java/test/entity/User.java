package test.entity;

import test.exception.EmptyNameException;

public class User {
  private String name;

  public User(String name) throws Exception {
    if (name == null || name == "") {
      throw new EmptyNameException();
    }
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
