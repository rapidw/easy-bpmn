package io.rapidw.easybpmn.process;

import io.rapidw.easybpmn.ProcessEngine;
import io.rapidw.easybpmn.process.operation.ContinueProcessOperation;
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
    @ManyToOne(cascade = CascadeType.ALL)
    private ProcessDefinition processDefinition;
    private boolean finished;

    //??
    private Integer activityId;

    @Getter
    @OneToMany(cascade = CascadeType.ALL)
    private List<Execution> executions;

//    private ActivityInstance.State state;

    private String variableString;

    @Transient
    private Object variable;

    public ProcessInstance(ProcessEngine processEngine, ProcessDefinition processDefinition, Variable variable) {
        this.processEngine = processEngine;
        this.executions = new LinkedList<>();

        this.processDefinition = processDefinition;
        val execution = Execution.builder().processInstance(this)
            .initialFlowElement(processDefinition.getProcess().getInitialFlowElement()).build();
        this.executions.add(execution);

        this.variable = variable;
        this.variableString = processEngine.getObjectMapper().writeValueAsString(variable);
    }

    public List<TaskInstance> queryTask(TaskQuery taskQuery) {
        return processEngine.queryTask(taskQuery.setProcessInstance(this));
    }

    public void start() {
        log.info("start process instance");
        val init = this.processDefinition.getProcess().getInitialFlowElement();

        val execution = Execution.builder().processInstance(this).initialFlowElement(init).build();

        processEngine.addOperation(ContinueProcessOperation.builder()
            .processDefinitionId(processDefinition.getId())
            .processInstanceId(this.getId())
            .executionId(execution.getId())
            .build());
    }
}
