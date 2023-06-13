package io.rapidw.easybpmn.engine.repository;

import io.rapidw.easybpmn.engine.HasId;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class AbstractRepository<T extends HasId> {
    protected ThreadLocal<EntityManager> entityManagerThreadLocal;
    public AbstractRepository(ThreadLocal<EntityManager> entityManagerThreadLocal) {
        this.entityManagerThreadLocal = entityManagerThreadLocal;
    }

    protected EntityManager getEntityManager() {
        return entityManagerThreadLocal.get();
    }

    public Integer persistAndGetId(T object) {
        log.debug("persistAndGetId {}", object.getClass().getSimpleName());
        val entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(object);
        entityManager.flush();
        entityManager.getTransaction().commit();
        return object.getId();
    }

    public void merge(T object) {
        log.debug("merge {}", object.getClass().getSimpleName());
        val entityManager = getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(object);
        entityManager.getTransaction().commit();
    }
}
