package test.repository;

import test.entity.User;

public interface IUserRepo {
  User findUserByName(String name); 
  void insert(User user);
}
