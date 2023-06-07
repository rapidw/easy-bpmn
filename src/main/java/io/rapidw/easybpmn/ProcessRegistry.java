package io.rapidw.easybpmn;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rapidw.easybpmn.engine.runtime.DeploymentQuery;
import io.rapidw.easybpmn.engine.serialization.Bpmn;
import io.rapidw.easybpmn.registry.Deployment;
import io.rapidw.easybpmn.registry.DeploymentService;
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
        this.deploymentService = new DeploymentService(entityManagerFactory);
    }

    @SneakyThrows
    public Integer deploy(String model) {
        val bpmn = this.objectMapper.readValue(model, Bpmn.class);
        if (!validate(bpmn)) {
            throw new ProcessEngineException("invalid bpmn model");
        }
        return deploymentService.deploy(Deployment.builder().name("test").model(model).build());
    }

    public List<Deployment> query(DeploymentQuery query) {
        return deploymentService.queryProcessDefinition(query);
    }

    private boolean validate(Bpmn bpmn) {
        return true;
    }
}
