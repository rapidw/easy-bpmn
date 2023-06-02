package io.rapidw.easybpmn.service;

import io.rapidw.easybpmn.process.Execution;
import jakarta.persistence.EntityManagerFactory;

public class ExecutionService extends AbstractRepositoryService<Execution> {
    public ExecutionService(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

}
