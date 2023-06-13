package io.rapidw.easybpmn.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.repository.ExecutionRepository;
import io.rapidw.easybpmn.engine.repository.ProcessDefinitionService;
import io.rapidw.easybpmn.engine.repository.ProcessInstanceRepository;
import io.rapidw.easybpmn.engine.repository.TaskRepository;
import io.rapidw.easybpmn.registry.DeploymentQuery;
import io.rapidw.easybpmn.engine.operation.AbstractOperation;
import io.rapidw.easybpmn.registry.ProcessRegistry;
import io.rapidw.easybpmn.task.TaskQuery;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

@Slf4j
public class ProcessEngine {

    //    @Getter
//    private final EntityManagerFactory entityManagerFactory;
    private final SessionFactory sessionFactory;
    @Getter
    private final ObjectMapper objectMapper;
    private final ProcessRegistry processRegistry;
    @Getter(AccessLevel.PACKAGE)
    private final ThreadLocal<EntityManager> entityManagerThreadLocal;

    private final OperationExecutor operationExecutor;


    //    @Getter
//    private final RuntimeService runtimeService;
    @Getter
    private final TaskRepository taskRepository;
    //    private final HistoryService historyService;
    @Getter
    private ProcessInstanceRepository processInstanceRepository;
    @Getter
    private ProcessDefinitionService processDefinitionService;
    @Getter
    private ExecutionRepository executionRepository;


    public ProcessEngine(ProcessRegistry processRegistry, ProcessEngineConfig config) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();
        this.sessionFactory = new MetadataSources(registry)
            .buildMetadata()
            .getSessionFactoryBuilder()
            .applyInterceptor(new SessionFactoryInterceptor(this))
            .build();

//        this.entityManagerFactory = Persistence.createEntityManagerFactory("easy-bpmn");
        this.objectMapper = new ObjectMapper();
        this.entityManagerThreadLocal = ThreadLocal.withInitial(sessionFactory::createEntityManager);

        this.processRegistry = processRegistry;
        this.operationExecutor = new OperationExecutor(this);

//        this.runtimeService = new RuntimeService(entityManagerFactory);
        this.taskRepository = new TaskRepository(entityManagerThreadLocal);
//        this.historyService = new HistoryService();
        this.processInstanceRepository = new ProcessInstanceRepository(entityManagerThreadLocal);
        this.processDefinitionService = new ProcessDefinitionService();
        this.executionRepository = new ExecutionRepository(entityManagerThreadLocal);
    }

    public void addOperation(AbstractOperation operation) {
        this.operationExecutor.addOperation(operation);
    }

    public ProcessInstance startProcessInstanceById(Integer id, Object variable) {
        log.info("start process instance by process id");
        val deployments = this.processRegistry.query(DeploymentQuery.builder().id(id).build());
        if (deployments.size() != 1) {
            throw new ProcessEngineException("invalid deployment id");
        }
        val definition = ProcessDefinition.builder().processEngine(this).deployment(deployments.get(0)).build();
        processDefinitionService.put(id, definition);
        val processInstance = new ProcessInstance(this, definition, variable);
        this.getProcessInstanceRepository().persistAndGetId(processInstance);
        processInstance.start();
        return processInstance;
    }


    public List<TaskInstance> queryTask(TaskQuery query) {
        try (EntityManager entityManager = sessionFactory.createEntityManager()) {
            return taskRepository.queryTask(entityManager, query);
        }
    }
}
