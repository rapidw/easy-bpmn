package io.rapidw.easybpmn.engine;

import com.google.common.collect.Lists;
import io.rapidw.easybpmn.engine.model.FlowElement;
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

    @Transient
    @Setter
    @Getter
    private ProcessEngine processEngine;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Integer id;

    @ToString.Exclude
    @ManyToOne(cascade = CascadeType.REFRESH)
    @Getter
    @JoinColumn(name = "process_instance_id")
    private ProcessInstance processInstance;

    // flowElement id persist??
    @Getter
    @Setter
    private String currentFlowElementId;

    @ManyToOne(cascade = CascadeType.ALL)
    @Getter
    private Execution parent;

    @Getter
    @OneToMany(mappedBy = "parent")
    private List<Execution> children = Lists.newArrayList();

    // todo: use it
    @Getter
    @Setter
    private boolean active;

    @ManyToOne
    @Getter
    @Setter
    private Variable variable;

    @Builder
    public Execution(ProcessInstance processInstance, FlowElement initialFlowElement, Execution parent, Variable variable, boolean active) {
        this.processInstance = processInstance;
        this.currentFlowElementId = initialFlowElement.getId();
        this.parent = parent;
        this.children = new ArrayList<>();
        this.active = active;
        this.variable = variable;
    }
}
