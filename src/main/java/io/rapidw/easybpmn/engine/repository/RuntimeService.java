package io.rapidw.easybpmn.engine.repository;

import jakarta.persistence.EntityManagerFactory;

public class RuntimeService {

    private EntityManagerFactory entityManagerFactory;

    public RuntimeService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
}
