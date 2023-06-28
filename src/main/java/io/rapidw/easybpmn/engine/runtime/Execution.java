package io.rapidw.easybpmn.engine.runtime;

import com.google.common.collect.Lists;
import io.rapidw.easybpmn.engine.ProcessEngine;
import io.rapidw.easybpmn.engine.model.FlowElement;
import jakarta.persistence.*;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "execution")
public class Execution {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Execution.class);
    @Transient
    private ProcessEngine processEngine;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "process_instance_id")
    private ProcessInstance processInstance;

    private String currentFlowElementId;

    @ManyToOne(cascade = CascadeType.ALL)
    private Execution parent;

    @OneToMany(mappedBy = "parent")
    private List<Execution> children = Lists.newArrayList();

    private boolean active;

    @ManyToOne
    private Variable variable;

    public Execution(ProcessInstance processInstance, FlowElement initialFlowElement, Execution parent, Variable variable, boolean active) {
        this.processInstance = processInstance;
        this.currentFlowElementId = initialFlowElement.getId();
        this.parent = parent;
        this.children = new ArrayList<>();
        this.active = active;
        this.variable = variable;
    }

    public Execution() {
    }

    public static ExecutionBuilder builder() {
        return new ExecutionBuilder();
    }

    public ProcessEngine getProcessEngine() {
        return this.processEngine;
    }

    public Long getId() {
        return this.id;
    }

    public ProcessInstance getProcessInstance() {
        return this.processInstance;
    }

    public String getCurrentFlowElementId() {
        return this.currentFlowElementId;
    }

    public Execution getParent() {
        return this.parent;
    }

    public List<Execution> getChildren() {
        return this.children;
    }

    public boolean isActive() {
        return this.active;
    }

    public Variable getVariable() {
        return this.variable;
    }

    public void setProcessEngine(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    public void setCurrentFlowElementId(String currentFlowElementId) {
        log.debug("set execution {} current flow element id: {}", id, currentFlowElementId);
        this.currentFlowElementId = currentFlowElementId;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    public static class ExecutionBuilder {
        private ProcessInstance processInstance;
        private FlowElement initialFlowElement;
        private Execution parent;
        private Variable variable;
        private boolean active;

        ExecutionBuilder() {
        }

        public ExecutionBuilder processInstance(ProcessInstance processInstance) {
            this.processInstance = processInstance;
            return this;
        }

        public ExecutionBuilder initialFlowElement(FlowElement initialFlowElement) {
            this.initialFlowElement = initialFlowElement;
            return this;
        }

        public ExecutionBuilder parent(Execution parent) {
            this.parent = parent;
            return this;
        }

        public ExecutionBuilder variable(Variable variable) {
            this.variable = variable;
            return this;
        }

        public ExecutionBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public Execution build() {
            return new Execution(this.processInstance, this.initialFlowElement, this.parent, this.variable, this.active);
        }

        public String toString() {
            return "Execution.ExecutionBuilder(processInstance=" + this.processInstance + ", initialFlowElement=" + this.initialFlowElement + ", parent=" + this.parent + ", variable=" + this.variable + ", active=" + this.active + ")";
        }
    }
}
