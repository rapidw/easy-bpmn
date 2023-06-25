package io.rapidw.easybpmn.engine.runtime;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.*;
import io.rapidw.easybpmn.engine.operation.LeaveFlowElementOperation;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Entity
@Slf4j
@NoArgsConstructor
@Table(name = "task_instance")
public class TaskInstance {

    @Transient
    @Getter
    @Setter
    private ProcessEngine processEngine;

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "process_instance_id")
    private ProcessInstance processInstance;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "execution_id")
    @Getter
    private Execution execution;

    @Getter
    private String userTaskId;

    // effective executor
    private String executor;
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @Setter
    @Getter
    @JoinColumn(name = "variable_id")
    private Variable variable;

    private Instant startTime;
    private Instant finishTime;

    @Builder
    public TaskInstance(Execution execution, String userTaskId, String executor, String name, Variable variable) {
        this.processInstance = execution.getProcessInstance();
        this.execution = execution;
        this.executor = executor;
        this.name = name;
        this.variable = variable;
        this.userTaskId = userTaskId;
    }

    public void complete(Object variableObject) {

        val transaction = this.processEngine.getEntityManagerThreadLocal().get().getTransaction();
        transaction.begin();
        // save variable
        var variable = new Variable(processEngine.getObjectMapper(), variableObject);

        this.setVariable(variable);
        // new execution and set variable
        val child = Execution.builder()
            .processInstance(this.getExecution().getProcessInstance())
            .parent(this.getExecution())
            .initialFlowElement(processEngine.getProcessDefinitionManager().get(this.getExecution().getProcessInstance().getDeploymentId()).getProcess().getFlowElementMap().get(this.getUserTaskId()))
            .variable(variable)
            .active(true)
            .build();
        this.getExecution().getChildren().add(child);
        this.processEngine.getExecutionRepository().persist(child);
        this.getExecution().setActive(false);

        // set variable to process instance
//        this.processInstance.getVariable().setJson(this.variable.getJson());
        this.getExecution().getProcessInstance().setVariable(variable);
//        processInstanceService.updateVariable(taskInstance.getExecution().getProcessInstance(), variable);

        transaction.commit();

        processEngine.addOperation(LeaveFlowElementOperation.builder()
            .executionId(child.getId())
            .build());
        log.debug("completing task instance: {}", this.id);
    }

    public <T> T getVariable(Class<T> clazz) {
        if (this.variable == null) {
            this.variable = this.execution.getVariable();
        }
        if (!this.variable.getClazz().equals(clazz.getName())) {
            throw new ProcessEngineException("variable class not the same");
        }
        try {
            return this.processEngine.getObjectMapper().readValue(this.variable.getData(), clazz);
        } catch (JsonProcessingException e) {
            throw new ProcessEngineException("failed to deserialize variable", e);
        }
    }
}
