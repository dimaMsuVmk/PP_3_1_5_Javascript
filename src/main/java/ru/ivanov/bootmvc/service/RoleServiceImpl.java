package ru.ivanov.bootmvc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ivanov.bootmvc.dao.RoleDao;
import ru.ivanov.bootmvc.model.Role;

import java.util.List;
@Service
public class RoleServiceImpl implements RoleService{

    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Transactional
    @Override
    public void save(Role role) {
        roleDao.save(role);
    }
}
