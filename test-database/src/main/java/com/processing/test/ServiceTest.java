package com.processing.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Service
@Slf4j
public class ServiceTest {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    public void init() {
        final Test testEntityInit = new Test();
        testEntityInit.setId(1L);
        testEntityInit.setCounter(0);
        entityManager.persist(testEntityInit);
    }


    @Transactional
    public void updateCounterNotSafe() {
        log.info("Before update");
        final Test testEntity = entityManager.find(Test.class, 1L);
        final Query query = entityManager.createQuery("update Test t set t.counter = :counter");
        query.setParameter("counter", testEntity.getCounter() + 1);
        query.executeUpdate();
        log.info("After update");
    }

    /**
     * https://blog.pjam.me/posts/atomic-operations-in-sql/
     */
    @Transactional
    public void updateCounterSafe() {
        log.info("Before update");
        final Test testEntity = entityManager.find(Test.class, 1L);
        final Query query = entityManager.createQuery("update Test t set t.counter = t.counter +1 ");
        query.executeUpdate();
        log.info("After update");
    }

}
