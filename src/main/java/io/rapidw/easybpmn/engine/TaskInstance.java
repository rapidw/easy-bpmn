package io.rapidw.easybpmn.engine;

import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.model.UserTask;
import io.rapidw.easybpmn.engine.operation.TakeOutgoingSequenceFlowEngineOperation;
import jakarta.persistence.*;
import lombok.*;
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

    @SneakyThrows
    public <T> T getVariable(Class<T> clazz) {
        val className = this.processInstance.getVariable().getClazz();
        if (className.equals(clazz.getName())) {
            return this.processInstance.getProcessEngine().getObjectMapper().readValue(this.variable.getJson(), clazz);
        } else {
            throw new ProcessEngineException("variable type mismatch");
        }
    }

    public void complete(Object variable) {
        this.processInstance.getProcessEngine().addOperation(TakeOutgoingSequenceFlowEngineOperation.builder()
            .executionId(execution.getId())
            .build());
        log.debug("completing task instance: {}", this.id);
    }
}
