package io.rapidw.easybpmn;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rapidw.easybpmn.engine.runtime.ProcessDefinition;
import io.rapidw.easybpmn.engine.runtime.DeploymentQuery;
import io.rapidw.easybpmn.engine.runtime.ProcessInstance;
import io.rapidw.easybpmn.engine.runtime.TaskInstance;
import io.rapidw.easybpmn.engine.runtime.operation.Operation;
import io.rapidw.easybpmn.engine.service.*;
import io.rapidw.easybpmn.task.TaskQuery;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class ProcessEngine {

    @Getter
    private final EntityManagerFactory entityManagerFactory;
    @Getter
    private ObjectMapper objectMapper;
    private ProcessRegistry processRegistry;

    @Getter
    private final RuntimeService runtimeService;
    @Getter
    private final TaskService taskService;
    private final HistoryService historyService;
    @Getter
    private ProcessInstanceService processInstanceService;
    @Getter
    private ProcessDefinitionService processDefinitionService;

    private final LinkedBlockingQueue<Operation> operations;
    private final Thread worker;

    public ProcessEngine(ProcessRegistry processRegistry, ProcessEngineConfig config) {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("easy-bpmn");
        this.objectMapper = new ObjectMapper();

        this.processRegistry = processRegistry;
        this.runtimeService = new RuntimeService(entityManagerFactory);
        this.taskService = new TaskService();
        this.historyService = new HistoryService();
        this.processInstanceService = new ProcessInstanceService(entityManagerFactory);
        this.processDefinitionService = new ProcessDefinitionService(entityManagerFactory);

        operations = new LinkedBlockingQueue<>();

        log.info("engine start");
        worker = new Thread(() -> {
            while (true) {
                try {
                    val operation = operations.take();
                    log.debug("new operation {}", operation.getClass().getSimpleName());
                    operation.execute(this);
                } catch (InterruptedException e) {
                    throw new ProcessEngineException(e);
                }
            }
        });
        worker.start();
    }

    public void addOperation(Operation operation) {
        try {
            operations.put(operation);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public ProcessInstance startProcessInstanceById(Integer id, Object variable) {
        log.info("start process instance by process id");
        val deployments = this.processRegistry.query(DeploymentQuery.builder().id(id).build());
        if (deployments.size() != 1) {
            throw new ProcessEngineException("invalid deployment id");
        }
        val definition = ProcessDefinition.builder().processEngine(this).deployment(deployments.get(0)).build();
        val processInstance = new ProcessInstance(this, definition, variable);
        this.getProcessInstanceService().persistAndGetId(processInstance);
        processInstance.start();
        return processInstance;
    }


    public List<TaskInstance> queryTask(TaskQuery query) {
        return taskService.queryTask(query);
    }


}
