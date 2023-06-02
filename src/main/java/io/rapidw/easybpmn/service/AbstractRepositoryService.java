package io.rapidw.easybpmn.service;

import io.rapidw.easybpmn.process.HasId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class AbstractRepositoryService<T extends HasId> {

    private final EntityManagerFactory entityManagerFactory;

    public AbstractRepositoryService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Integer persistAndGetId(T object) {
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(object);
            em.flush();
            em.getTransaction().commit();
            return object.getId();
        }
    }
}
