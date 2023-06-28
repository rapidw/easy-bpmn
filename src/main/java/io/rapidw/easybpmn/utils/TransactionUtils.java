package io.rapidw.easybpmn.utils;

import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.ProcessEngine;
import jakarta.persistence.EntityManager;

import java.util.function.Supplier;

public class TransactionUtils {

    public static <V> V callWithTransaction(ProcessEngine processEngine, Supplier<V> supplier) {
        EntityManager entityManager = processEngine.getEntityManagerThreadLocal().get();
        entityManager.getTransaction().begin();
        try {
            V v = supplier.get();
            entityManager.getTransaction().commit();
            return v;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new ProcessEngineException(e);
        }
    }
}
