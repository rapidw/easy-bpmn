package io.rapidw.easybpmn.engine.repository;

import io.rapidw.easybpmn.engine.runtime.HasId;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AbstractRepository<T extends HasId> {

    public Integer persistAndGetId(EntityManager entityManager, T object) {
        log.debug("persistAndGetId {}", object.getClass().getSimpleName());
        entityManager.getTransaction().begin();
        entityManager.persist(object);
        entityManager.flush();
        entityManager.getTransaction().commit();
        return object.getId();
    }

    public void merge(EntityManager entityManager, T object) {
        log.debug("merge {}", object.getClass().getSimpleName());
        entityManager.getTransaction().begin();
        entityManager.merge(object);
        entityManager.getTransaction().commit();
    }
}
