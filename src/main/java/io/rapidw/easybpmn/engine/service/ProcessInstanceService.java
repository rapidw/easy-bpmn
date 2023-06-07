package io.rapidw.easybpmn.engine.service;

import io.rapidw.easybpmn.engine.runtime.ProcessInstance;
import jakarta.persistence.EntityManagerFactory;

public class ProcessInstanceService extends AbstractRepositoryService<ProcessInstance> {
    public ProcessInstanceService(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }


}
