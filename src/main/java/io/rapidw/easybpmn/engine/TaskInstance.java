package io.rapidw.easybpmn.engine;

import io.rapidw.easybpmn.engine.model.UserTask;
import io.rapidw.easybpmn.engine.operation.TakeOutgoingSequenceFlowOperation;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Slf4j
@NoArgsConstructor
@Table(name = "task_instance")
public class TaskInstance implements HasId {

    @Builder
    public TaskInstance(Execution execution, UserTask userTask, String assignee, String name, Variable variable) {
        this.processInstance = execution.getProcessInstance();
        this.execution = execution;
        this.assignee = assignee;
        this.name = name;
        this.variable = variable;
    }

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private ProcessInstance processInstance;

    @ManyToOne
    private Execution execution;

    @Embedded
    private UserTask userTask;

    private String assignee;
    private String name;

    @Embedded
    private Variable variable;

    public void complete(Object variable) {
        this.processInstance.getProcessEngine().addOperation(TakeOutgoingSequenceFlowOperation.builder()
            .processInstanceId(processInstance.getId())
            .executionId(execution.getId())
            .processDefinitionId(processInstance.getProcessDefinition().getId())
            .build());
        log.debug("completing task instance: {}", this.id);
    }
}
