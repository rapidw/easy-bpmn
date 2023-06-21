package io.rapidw.easybpmn.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.operation.TakeOutgoingSequenceFlowEngineOperation;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Entity
@Slf4j
@NoArgsConstructor
@Table(name = "task_instance")
public class TaskInstance implements HasId {

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

    private String assignee;
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @Setter
    @Getter
    @JoinColumn(name = "variable_id")
    private Variable variable;

    @Builder
    public TaskInstance(Execution execution, String userTaskId, String assignee, String name, Variable variable) {
        this.processInstance = execution.getProcessInstance();
        this.execution = execution;
        this.assignee = assignee;
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
        val newExecution = Execution.builder()
            .processInstance(this.getExecution().getProcessInstance())
            .parent(this.getExecution())
            .initialFlowElement(processEngine.getProcessDefinitionManager().get(this.getExecution().getProcessInstance().getDeploymentId()).getProcess().getFlowElementMap().get(this.getUserTaskId()))
            .variable(variable)
            .build();
        this.getExecution().getChildren().add(newExecution);
        this.processEngine.getExecutionRepository().persist(newExecution);

        // set variable to process instance
//        this.processInstance.getVariable().setJson(this.variable.getJson());
        this.getExecution().getProcessInstance().setVariable(variable);
//        processInstanceService.updateVariable(taskInstance.getExecution().getProcessInstance(), variable);

        transaction.commit();

        processEngine.addOperation(TakeOutgoingSequenceFlowEngineOperation.builder()
            .executionId(newExecution.getId())
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
