package io.rapidw.easybpmn;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rapidw.easybpmn.engine.repository.*;
import io.rapidw.easybpmn.engine.runtime.*;
import io.rapidw.easybpmn.engine.runtime.operation.AbstractOperation;
import io.rapidw.easybpmn.task.TaskQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.List;

@Slf4j
public class ProcessEngine {

    @Getter
    private final EntityManagerFactory entityManagerFactory;
    @Getter
    private final ObjectMapper objectMapper;
    private final ProcessRegistry processRegistry;

    private final OperationExecutor operationExecutor;


    @Getter
    private final RuntimeService runtimeService;
    @Getter
    private final TaskRepository taskRepository;
    private final HistoryService historyService;
    @Getter
    private ProcessInstanceRepository processInstanceRepository;
    @Getter
    private ProcessDefinitionService processDefinitionService;
    @Getter
    private ExecutionRepository executionRepository;



    public ProcessEngine(ProcessRegistry processRegistry, ProcessEngineConfig config) {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("easy-bpmn");
        this.objectMapper = new ObjectMapper();

        this.processRegistry = processRegistry;
        this.operationExecutor = new OperationExecutor(this);

        this.runtimeService = new RuntimeService(entityManagerFactory);
        this.taskRepository = new TaskRepository();
        this.historyService = new HistoryService();
        this.processInstanceRepository = new ProcessInstanceRepository();
        this.processDefinitionService = new ProcessDefinitionService();
        this.executionRepository = new ExecutionRepository();
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
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            this.getProcessInstanceRepository().persistAndGetId(entityManager, processInstance);
        }
        processInstance.start();
        return processInstance;
    }


    public List<TaskInstance> queryTask(TaskQuery query) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return taskRepository.queryTask(entityManager, query);
        }
    }
}
