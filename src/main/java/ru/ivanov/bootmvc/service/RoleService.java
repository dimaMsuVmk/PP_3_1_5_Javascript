package ru.ivanov.bootmvc.service;

import ru.ivanov.bootmvc.model.Role;

import java.util.List;

public interface RoleService {

    List<Role> findAll();

    void save(Role role);
}
