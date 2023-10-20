package ru.ivanov.bootmvc.dao;

import ru.ivanov.bootmvc.model.Role;

import java.util.List;
import java.util.NoSuchElementException;

public interface RoleDao {
    List<Role> findAll();

    void save(Role role);
}
