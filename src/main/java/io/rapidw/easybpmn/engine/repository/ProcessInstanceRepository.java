package io.rapidw.easybpmn.engine.repository;

import io.rapidw.easybpmn.engine.ProcessInstance;
import jakarta.persistence.EntityManager;

public class ProcessInstanceRepository extends AbstractRepository<ProcessInstance> {
    public ProcessInstanceRepository(ThreadLocal<EntityManager> entityManagerThreadLocal) {
        super(entityManagerThreadLocal);
    }
}
