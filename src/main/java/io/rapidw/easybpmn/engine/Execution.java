package io.rapidw.easybpmn.engine;

import io.rapidw.easybpmn.engine.model.FlowElement;
import io.rapidw.easybpmn.engine.repository.ExecutionRepository;
import io.rapidw.easybpmn.engine.repository.TaskRepository;
import io.rapidw.easybpmn.task.TaskQuery;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity
@Table(name = "execution")
@NoArgsConstructor
public class Execution implements HasId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Integer id;

    @ToString.Exclude
    @ManyToOne
    @Getter
    @JoinColumn(name = "process_instance_id")
    private ProcessInstance processInstance;

    @Transient
    @Getter
    private TaskRepository taskRepository;

    @Transient
    private ExecutionRepository executionRepository;

    // flowElement id persist??
    @Getter
    @Setter
    private String currentFlowElementId;

    @ManyToOne
    @Getter
    private Execution parent;

    @OneToMany(mappedBy = "parent")
    private List<Execution> children;

    // todo: use it
    @Getter
    @Setter
    private boolean active;

    @Embedded
    private Variable variable;

    @Builder
    public Execution(ProcessInstance processInstance, FlowElement initialFlowElement, Execution parent, Variable variable, boolean active) {
        this.processInstance = processInstance;
        this.executionRepository = processInstance.getProcessEngine().getExecutionRepository();
        this.taskRepository = processInstance.getProcessEngine().getTaskRepository();
        this.currentFlowElementId = initialFlowElement.getId();
        this.parent = parent;
        this.children = new ArrayList<>();
        this.active = active;
        this.variable = variable;
    }

    public TaskInstance queryTask(TaskQuery taskQuery) {
        return null;
    }

    public void addChild(Execution execution) {
        this.children.add(execution);
    }

    public void merge() {
        this.executionRepository.merge(this);
    }

    public <T> T getVariable(Class<T> clazz) {
        if (variable != null) {
            return variable.getVariable(this.processInstance.getProcessEngine().getObjectMapper(), clazz);
        } else {
            return null;
        }
    }
}
