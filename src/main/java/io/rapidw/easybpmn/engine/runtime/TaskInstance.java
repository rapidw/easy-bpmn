package io.rapidw.easybpmn.engine.runtime;

import io.rapidw.easybpmn.engine.model.UserTask;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Entity
@Slf4j
@NoArgsConstructor
public class TaskInstance implements HasId {

    @Builder
    public TaskInstance(ProcessInstance processInstance, Execution execution, UserTask userTask, String assignee, String name, Variable variable) {
        this.processInstance = processInstance;
        this.execution = execution;
        this.assignee = assignee;
        this.name = name;
        this.variable = variable;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Transient
    private ProcessInstance processInstance;
    @Transient
    private Execution execution;

    @Embedded
    private UserTask userTask;

    private String assignee;
    private String name;

    @Embedded
    private Variable variable;

    public void complete(Object variable) {
        log.debug("completing task instance: {}", this.id);
    }
}
