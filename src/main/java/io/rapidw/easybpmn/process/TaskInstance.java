package io.rapidw.easybpmn.process;

import lombok.Data;

// T: type of task variable
@Data
public class TaskInstance<T, E> {
    private Integer id;
    private ProcessInstance<E> processInstance;
    private Execution execution;
    private String assignee;
    private String name;

    private T variable;
    public void complete(T variable) {

    }
}
