package io.rapidw.easybpmn.engine.service;

import io.rapidw.easybpmn.engine.runtime.Execution;
import jakarta.persistence.EntityManagerFactory;

public class ExecutionService extends AbstractRepositoryService<Execution> {
    public ExecutionService(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

}
