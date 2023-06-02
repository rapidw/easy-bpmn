package io.rapidw.easybpmn.service;

import jakarta.persistence.EntityManagerFactory;

public class RuntimeService {

    private EntityManagerFactory entityManagerFactory;

    public RuntimeService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
}
