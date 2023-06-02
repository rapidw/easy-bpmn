package io.rapidw.easybpmn;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rapidw.easybpmn.process.ProcessDefinition;
import io.rapidw.easybpmn.process.ProcessInstance;
import io.rapidw.easybpmn.process.TaskInstance;
import io.rapidw.easybpmn.process.Variable;
import io.rapidw.easybpmn.process.operation.Operation;
import io.rapidw.easybpmn.service.HistoryService;
import io.rapidw.easybpmn.service.ProcessInstanceService;
import io.rapidw.easybpmn.service.RuntimeService;
import io.rapidw.easybpmn.service.TaskService;
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

    @Getter
    private final RuntimeService runtimeService;
    @Getter
    private final TaskService taskService;
    private final HistoryService historyService;
    @Getter
    private ProcessInstanceService processInstanceService;

    private final LinkedBlockingQueue<Operation> operations;
    private final Thread worker;

    public ProcessEngine(ProcessEngineConfig config) {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("easy-bpmn");
        this.objectMapper = new ObjectMapper();

        this.runtimeService = new RuntimeService(entityManagerFactory);
        this.taskService = new TaskService();
        this.historyService = new HistoryService();
        this.processInstanceService = new ProcessInstanceService(entityManagerFactory);

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

    public ProcessInstance startProcessInstance(ProcessDefinition processDefinition, Variable variable) {
        log.info("start process instance by process definition");
        val processInstance = new ProcessInstance(this, processDefinition, variable);
        this.getProcessInstanceService().persistAndGetId(processInstance);
        processInstance.start();
        return processInstance;
    }


    public List<TaskInstance> queryTask(TaskQuery query) {
        return taskService.queryTask(query);
    }


}
