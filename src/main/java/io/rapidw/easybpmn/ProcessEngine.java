package io.rapidw.easybpmn;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rapidw.easybpmn.engine.repository.*;
import io.rapidw.easybpmn.engine.runtime.*;
import io.rapidw.easybpmn.engine.runtime.operation.Operation;
import io.rapidw.easybpmn.task.TaskQuery;
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
    private ObjectMapper objectMapper;
    private ProcessRegistry processRegistry;

    private OperationExecutor operationExecutor;

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
        this.taskRepository = new TaskRepository(entityManagerFactory);
        this.historyService = new HistoryService();
        this.processInstanceRepository = new ProcessInstanceRepository(entityManagerFactory);
        this.processDefinitionService = new ProcessDefinitionService();
        this.executionRepository = new ExecutionRepository(entityManagerFactory);
    }

    public void addOperation(Operation operation) {
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
        return taskRepository.queryTask(query);
    }


}
