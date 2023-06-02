package io.rapidw.easybpmn.service;

import io.rapidw.easybpmn.Deployment;
import io.rapidw.easybpmn.process.ProcessDefinition;
import io.rapidw.easybpmn.process.ProcessDefinitionQuery;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.List;

@Slf4j
public class DeploymentService {

    private final EntityManagerFactory entityManagerFactory;
    public DeploymentService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public void deploy(Deployment deployment) {
        log.debug("deploy start");
        val entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(deployment);
        entityManager.flush();
        entityManager.getTransaction().commit();
        log.debug("deploy finished");
    }

    public List<ProcessDefinition> queryProcessDefinition(ProcessDefinitionQuery query) {
        return null;
    }
}
