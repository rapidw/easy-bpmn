package io.rapidw.easybpmn.service;

import io.rapidw.easybpmn.process.ProcessInstance;
import jakarta.persistence.EntityManagerFactory;

public class ProcessInstanceService extends AbstractRepositoryService<ProcessInstance> {
    public ProcessInstanceService(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }


}
