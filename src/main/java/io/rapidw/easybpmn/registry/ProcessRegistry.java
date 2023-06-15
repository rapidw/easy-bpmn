package io.rapidw.easybpmn.registry;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.serialization.Bpmn;
import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

@Slf4j
public class ProcessRegistry {
    private ProcessRegistryConfig config;
//    private EntityManagerFactory entityManagerFactory;
    private SessionFactory sessionFactory;
    @Getter
    private final ObjectMapper objectMapper;
    @Getter
    private final DeploymentService deploymentService;

    public ProcessRegistry(ProcessRegistryConfig config) {
        this.config = config;
//        this.entityManagerFactory = Persistence.createEntityManagerFactory("easy-bpmn");
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();
        this.sessionFactory = new MetadataSources(registry)
            .addAnnotatedClasses(Deployment.class)
            .buildMetadata().buildSessionFactory();
        this.objectMapper = new ObjectMapper();
        this.deploymentService = new DeploymentService(sessionFactory);
    }

    @SneakyThrows
    public Integer deploy(String model) {
        val bpmn = this.objectMapper.readValue(model, Bpmn.class);
        if (!validate(bpmn)) {
            throw new ProcessEngineException("invalid bpmn model");
        }
        try (EntityManager entityManager = sessionFactory.createEntityManager()) {
            return deploymentService.deploy(entityManager,
                Deployment.builder().name("test").model(model).build());
        }
    }

    public List<Deployment> query(DeploymentQuery query) {
        return deploymentService.queryProcessDefinition(query);
    }

    private boolean validate(Bpmn bpmn) {
        return true;
    }
}
