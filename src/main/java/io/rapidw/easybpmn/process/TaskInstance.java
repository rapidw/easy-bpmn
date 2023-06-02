package io.rapidw.easybpmn.process;

import lombok.Data;

// T: type of task variable
@Data
public class TaskInstance {
    private Integer id;
    private ProcessInstance processInstance;
    private Execution execution;
    private String assignee;
    private String name;

    private Object variable;
    public void complete(Object variable) {

    }
}
