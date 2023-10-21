package ru.ivanov.bootmvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ivanov.bootmvc.dao.UserDao;
import ru.ivanov.bootmvc.model.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public User getUserById(long id) {
        return userDao.getUserById(id);
    }

    @Override
    public User findByUserName(String email) {
        return userDao.findByEmail(email).get();
    }

    @Transactional
    @Override
    public void updateUser(User updateUser) {
        if (updateUser.getPassword() != null && updateUser.getPassword().length() != 0) {
            String encodedPassword = passwordEncoder.encode(updateUser.getPassword());
            updateUser.setPassword(encodedPassword);
        } else updateUser.setPassword(getEncodedPassword(updateUser.getId()));
        userDao.updateUser(updateUser);
    }

    @Transactional
    @Override
    public boolean removeUserById(long id) {
        return userDao.removeUserById(id);
    }

    @Transactional
    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public String getEncodedPassword(Long id) {
        return userDao.getPassword(id);
    }
}