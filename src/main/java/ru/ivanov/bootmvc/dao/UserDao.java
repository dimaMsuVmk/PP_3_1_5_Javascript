package ru.ivanov.bootmvc.dao;

import ru.ivanov.bootmvc.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    List<User> getAllUsers();

    User getUserById(long id);

    void updateUser(User updateUser);

    boolean removeUserById(long id);

    void save(User user);

    String getPassword(Long id);

    Optional<User> findByEmail(String email);
}