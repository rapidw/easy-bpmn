package io.rapidw.easybpmn.engine.runtime;

import io.rapidw.easybpmn.engine.model.FlowElement;
import io.rapidw.easybpmn.engine.service.TaskService;
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
    @GeneratedValue
    @Getter
    private Integer id;

    @ToString.Exclude
    @ManyToOne
    @Getter
    private ProcessInstance processInstance;

    @Transient
    @Getter
    private TaskService taskService;

    // flowElement id persist??
    @Transient
    @Getter
    @Setter
    private FlowElement currentFlowElement;

    @ManyToOne
    @Getter
    private Execution parent;

    @OneToMany(mappedBy = "parent")
    private List<Execution> children;

    @Getter
    @Setter
    private boolean active;

    @Builder
    public Execution(ProcessInstance processInstance, FlowElement initialFlowElement, Execution parent, boolean active) {
        this.processInstance = processInstance;
        this.taskService = processInstance.getProcessEngine().getTaskService();
        this.currentFlowElement = initialFlowElement;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.active = active;
    }

    public TaskInstance queryTask(TaskQuery taskQuery) {
        return null;
    }

    public void addChild(Execution execution) {
        this.children.add(execution);
    }

}
