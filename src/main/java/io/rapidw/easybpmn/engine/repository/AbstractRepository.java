package io.rapidw.easybpmn.engine.repository;

import io.rapidw.easybpmn.engine.runtime.HasId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AbstractRepository<T extends HasId> {

    protected final EntityManagerFactory entityManagerFactory;

    public AbstractRepository(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Integer persistAndGetId(T object) {
        log.debug("persistAndGetId {}" , object.getClass().getSimpleName());
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(object);
            em.flush();
            em.getTransaction().commit();
            return object.getId();
        }
    }

    public void merge(T object) {
        log.debug("merge {}" , object.getClass().getSimpleName());
        try (EntityManager em = entityManagerFactory.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(object);
            em.getTransaction().commit();
        }
    }
}
