package ru.ivanov.bootmvc.dao;

import org.springframework.stereotype.Repository;
import ru.ivanov.bootmvc.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getAllUsers() {
        TypedQuery<User> query = entityManager
                .createQuery("SELECT DISTINCT u FROM User u left join fetch u.roles", User.class);
        return query.getResultList();
    }

    @Override
    public User getUserById(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void updateUser(User updateUser) {
        entityManager.merge(updateUser);
    }

    @Override
    public boolean removeUserById(long id) {
        return entityManager.createQuery("delete from User where id = :id")
                .setParameter("id", id).executeUpdate() > 0;
    }

    @Override
    public String getPassword(Long id) {
        return entityManager.createQuery("select u.password from User u where id = :id", String.class)
                .setParameter("id", id).getSingleResult();
    }

    @Override
    public void save(User user) {
        entityManager.persist(user);
    }


    @Override
    public Optional<User> findByEmail(String email) {
        //getSingleResult() - бросит ошибку если такого user нет
        return Optional.of((User) entityManager
                .createQuery("from User u join fetch u.roles where u.email = :email")
                .setParameter("email", email)
                .getSingleResult());
    }
}