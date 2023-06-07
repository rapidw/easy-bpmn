package io.rapidw.easybpmn.engine.runtime;

import io.rapidw.easybpmn.ProcessEngine;
import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.runtime.operation.ContinueProcessOperation;
import io.rapidw.easybpmn.task.TaskQuery;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Entity
@NoArgsConstructor
@Table(name = "process_instance")
public class ProcessInstance implements HasId {

    @Transient
    @Getter
    private ProcessEngine processEngine;

    @Getter
    @Id
    @GeneratedValue
    private Integer id;

    @Getter
    @Transient
    private ProcessDefinition processDefinition;
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

        this.processDefinition = processDefinition;
        val execution = Execution.builder().processInstance(this)
            .initialFlowElement(processDefinition.getProcess().getInitialFlowElement()).build();
        this.executions.add(execution);

        this.variable = new Variable();
        this.variable.setClazz(variableObj.getClass().getName());
        this.variable.setJson(processEngine.getObjectMapper().writeValueAsString(variableObj));
    }

    public List<TaskInstance> queryTask(TaskQuery taskQuery) {
        return processEngine.queryTask(taskQuery.setProcessInstance(this));
    }

    public void start() {
        log.info("start process instance");
        val init = this.processDefinition.getProcess().getInitialFlowElement();

        val execution = Execution.builder().processInstance(this).initialFlowElement(init).build();

        processEngine.addOperation(ContinueProcessOperation.builder()
//            .processDefinitionId(processDefinition.getId())
            .processInstanceId(this.getId())
            .executionId(execution.getId())
            .build());
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
