package io.rapidw.easybpmn.utils;

import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.ProcessEngine;
import jakarta.persistence.EntityManager;

import java.util.concurrent.Callable;

public class TransactionUtils {

    public static  <V> V runWithTransaction(ProcessEngine processEngine, Callable<V> callable) {
        EntityManager entityManager = processEngine.getEntityManagerThreadLocal().get();
        entityManager.getTransaction().begin();
        try {
            V v = callable.call();
            entityManager.getTransaction().commit();
            return v;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new ProcessEngineException(e);
        }
    }
}
