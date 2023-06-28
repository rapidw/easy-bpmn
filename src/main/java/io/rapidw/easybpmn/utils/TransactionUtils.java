package io.rapidw.easybpmn.utils;

import io.rapidw.easybpmn.engine.ProcessEngine;
import jakarta.persistence.EntityManager;
import lombok.val;

import java.util.function.Supplier;

public class TransactionUtils {

    public static <V> V callWithTransaction(ProcessEngine processEngine, Supplier<V> supplier) {
        EntityManager entityManager = processEngine.getEntityManagerThreadLocal().get();
        val transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            V v = supplier.get();
            transaction.commit();
            return v;
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}
