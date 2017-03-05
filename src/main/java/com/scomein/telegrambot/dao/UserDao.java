package com.scomein.telegrambot.dao;

import com.scomein.telegrambot.entities.User;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@Repository
public class UserDao {

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    @Transactional(readOnly=true)
    public User getById(long id) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        try {
            return  (User) sessionFactory
                    .getCurrentSession()
                    .getNamedQuery("getById")
                    .setParameter("id", id)
                    .uniqueResult();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            tx.commit();
        }

    }

    @Transactional
    public User save(User user) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(user);
            return user;
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            tx.commit();
        }
    }


}
