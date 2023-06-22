package io.rapidw.easybpmn.engine.runtime;

import io.rapidw.easybpmn.engine.ProcessEngine;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Entity
@NoArgsConstructor
@Table(name = "process_instance")
public class ProcessInstance {

    @Transient
    @Getter
    @Setter
    private ProcessEngine processEngine;

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    private Integer deploymentId;

    private boolean finished;

    //??
    private Integer activityId;

    @Getter
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "processInstance")
    private List<Execution> executions;

//    private ActivityInstance.State state;

    @ManyToOne(cascade = CascadeType.ALL)
    @Getter
    @Setter
    private Variable variable;

    @Version
    private Integer version;

    @SneakyThrows
    public ProcessInstance(Integer processDefinitionId, Variable variable) {
        this.executions = new LinkedList<>();
        this.deploymentId = processDefinitionId;
        this.variable = variable;
    }
}
