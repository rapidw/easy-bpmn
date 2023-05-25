package io.rapidw.easybpmn.process;

import io.rapidw.easybpmn.ProcessEngine;
import io.rapidw.easybpmn.task.TaskQuery;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class ProcessInstance<T> {

    private ProcessEngine processEngine;

    private Integer id;
    private ProcessDefinition processDefinition;
    private boolean finished;

    private Integer activityId;
    private List<Execution> executions;

    private ActivityInstance.State state;

    private T variable;

    public ProcessInstance(ProcessEngine processEngine, ProcessDefinition processDefinition, T variable) {
        this.processEngine = processEngine;
        this.executions = new LinkedList<>();

        this.processDefinition = processDefinition;
        this.executions.add(new Execution(this, processDefinition.getProcess().getInitialFlowElement()));

        this.variable = variable;
    }
    public List<TaskInstance<?, ?>> queryTask(TaskQuery taskQuery) {
        return processEngine.queryTask(taskQuery.setProcessInstance(this));
    }
}
