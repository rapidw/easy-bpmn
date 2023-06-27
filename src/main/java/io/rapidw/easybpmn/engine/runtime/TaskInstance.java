package io.rapidw.easybpmn.engine.runtime;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.ProcessEngine;
import io.rapidw.easybpmn.engine.operation.LeaveFlowElementOperation;
import io.rapidw.easybpmn.utils.TransactionUtils;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;

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
    private Long id;

    @Getter
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "process_instance_id")
    private ProcessInstance processInstance;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "execution_id")
    @Getter
    private Execution execution;

    @Getter
    private String userTaskId;

    @Getter
    @OneToMany
    private List<TaskCandidate> candidates;

    // effective executor
    @Getter
    private String assignee;
    @Getter
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @Setter
    @Getter
    @JoinColumn(name = "variable_id")
    private Variable variable;

    @Getter
    private Instant startTime;
    @Getter
    @Setter
    private Instant finishTime;


    @Builder
    public TaskInstance(Execution execution, String userTaskId, String assignee, List<TaskCandidate> candidates, String name, Variable variable) {
        this.processInstance = execution.getProcessInstance();
        this.execution = execution;
        this.assignee = assignee;
        this.candidates = candidates;
        this.name = name;
        this.variable = variable;
        this.userTaskId = userTaskId;
        this.startTime = Instant.now();
    }

    public void complete(Object variableObject) {
        // when task has candidate, it must has assignee when complete
        if (this.candidates != null && this.getAssignee() == null) {
            throw new ProcessEngineException("task has no assignee");
        }

        TransactionUtils.runWithTransaction(this.getProcessEngine(), () -> {

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

            processEngine.addOperation(LeaveFlowElementOperation.builder()
                .executionId(child.getId())
                .build());
            log.debug("completing task instance: {}", this.id);
            return null;
        });
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

    public void claim(String claimant) {
        if (this.assignee == null) {
            throw new ProcessEngineException("task has been claimed");
        } else {
            if (this.candidates == null) {
                throw new ProcessEngineException("no candidate found");
            } else {
                this.assignee = claimant;
            }
        }
    }
}
