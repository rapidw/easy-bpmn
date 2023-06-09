package io.rapidw.easybpmn;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rapidw.easybpmn.engine.runtime.DeploymentQuery;
import io.rapidw.easybpmn.engine.serialization.Bpmn;
import io.rapidw.easybpmn.registry.Deployment;
import io.rapidw.easybpmn.registry.DeploymentService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.List;

@Slf4j
public class ProcessRegistry {
    private ProcessRegistryConfig config;
    private EntityManagerFactory entityManagerFactory;
    @Getter
    private final ObjectMapper objectMapper;
    @Getter
    private final DeploymentService deploymentService;

    public ProcessRegistry(ProcessRegistryConfig config) {
        this.config = config;
        this.entityManagerFactory = Persistence.createEntityManagerFactory("easy-bpmn");
        this.objectMapper = new ObjectMapper();
        this.deploymentService = new DeploymentService();
    }

    @SneakyThrows
    public Integer deploy(String model) {
        val bpmn = this.objectMapper.readValue(model, Bpmn.class);
        if (!validate(bpmn)) {
            throw new ProcessEngineException("invalid bpmn model");
        }
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return deploymentService.deploy(entityManager,
                Deployment.builder().name("test").model(model).build());
        }
    }

    public List<Deployment> query(DeploymentQuery query) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return deploymentService.queryProcessDefinition(entityManager, query);
        }
    }

    private boolean validate(Bpmn bpmn) {
        return true;
    }
}
