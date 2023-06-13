package io.rapidw.easybpmn.engine;

import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.operation.ContinueProcessOperation;
import io.rapidw.easybpmn.task.TaskQuery;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Entity
@NoArgsConstructor
@Table(name = "process_instance")
public class ProcessInstance implements HasId {

    @Transient
    @Setter(AccessLevel.PACKAGE)
    @Getter
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

    @Embedded
    private Variable variable;

    @SneakyThrows
    public ProcessInstance(ProcessEngine processEngine, ProcessDefinition processDefinition, Object variableObj) {
        this.processEngine = processEngine;
        this.executions = new LinkedList<>();

        this.deploymentId = processDefinition.getId();
        val execution = Execution.builder()
            .processInstance(this)
            .initialFlowElement(processDefinition.getProcess().getInitialFlowElement())
            .active(true)
            .parent(null)
            .build();
        this.executions.add(execution);

        this.variable = new Variable();
        this.variable.setClazz(variableObj.getClass().getName());
        this.variable.setJson(processEngine.getObjectMapper().writeValueAsString(variableObj));
    }

    public ProcessDefinition getProcessDefinition() {
        return this.processEngine.getProcessDefinitionService().get(id);
    }

    public List<TaskInstance> queryTask(TaskQuery taskQuery) {
        return processEngine.queryTask(taskQuery.setProcessInstanceId(this.id));
    }

    public void start() {
        log.info("start process instance");

        processEngine.addOperation(ContinueProcessOperation.builder()
            .processDefinitionId(this.deploymentId)
            .processInstanceId(this.getId())
            .executionId(this.executions.get(0).getId())
            .build()
        );
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    <T> T getVariable(Class<T> tclazz) {

        val clazzStr = this.variable.getClazz();
        if (!clazzStr.equals(tclazz.getName())) {
            throw new ProcessEngineException("variable class not the same");
        }
        val clazz = Class.forName(clazzStr);
        return (T) this.processEngine.getObjectMapper().readValue(this.variable.getJson(), clazz);
    }
}
