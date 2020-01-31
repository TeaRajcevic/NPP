package com.rajcevic.tea.DiaryWebApp.data;

import com.rajcevic.tea.DiaryWebApp.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom{

@PersistenceContext
EntityManager em;

@Transactional
@Override
    public void saveUserWithAuthorities(User user) {
        em.persist(user);
        em.createNativeQuery("INSERT INTO AUTHORITIES (USERNAME, AUTHORITY) VALUES ( ?1 , 'ROLE_USER')")
                .setParameter(1, user.getUsername())
                .executeUpdate();
    }
}
