package io.rapidw.easybpmn.engine.service;

import jakarta.persistence.EntityManagerFactory;

public class RuntimeService {

    private EntityManagerFactory entityManagerFactory;

    public RuntimeService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
}
