package io.rapidw.easybpmn.engine.repository;

import io.rapidw.easybpmn.engine.Execution;
import jakarta.persistence.EntityManager;

public class ExecutionRepository extends AbstractRepository<Execution> {
    public ExecutionRepository(ThreadLocal<EntityManager> entityManagerThreadLocal) {
        super(entityManagerThreadLocal);
    }
}
