package io.rapidw.easybpmn.engine.repository;

import io.rapidw.easybpmn.ProcessEngineException;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.lang.reflect.ParameterizedType;

@Slf4j
public class AbstractRepository<T> {
    protected ThreadLocal<EntityManager> entityManagerThreadLocal;
    private final Class<T> clazz;

    @SuppressWarnings("unchecked")
    public AbstractRepository(ThreadLocal<EntityManager> entityManagerThreadLocal) {
        this.entityManagerThreadLocal = entityManagerThreadLocal;
        clazz = ((Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    protected EntityManager getEntityManager() {
        return entityManagerThreadLocal.get();
    }

    public void persist(T object) {
        log.debug("persist {}", object.getClass().getSimpleName());
        val entityManager = getEntityManager();
        entityManager.persist(object);
//        entityManager.flush();
//        return entityManager.find(clazz, object.getId());
    }

    public T merge(T object) {
        log.debug("merge {}", object.getClass().getSimpleName());
        val entityManager = getEntityManager();
        object = entityManager.merge(object);
        return object;
    }

    public T get(Long id) {
        log.debug("get {} by id {}", clazz.getSimpleName(), id);
        val entityManager = getEntityManager();
        val res = entityManager.find(clazz, id);
        if (res != null) {
            entityManager.refresh(res);
            return res;
        } else {
            throw new ProcessEngineException("cannot find " + clazz.getSimpleName() + " by id " + id);
        }
    }

    public void flush() {
        log.debug("flush {}", clazz.getSimpleName());
        val entityManager = getEntityManager();
        entityManager.flush();
    }
}
