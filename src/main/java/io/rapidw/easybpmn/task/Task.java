package io.rapidw.easybpmn.task;

import lombok.Data;

// T: type of task variable
@Data
public class Task<T> {
    private Integer id;
    private Integer processInstanceId;
    private String assignee;
    private String name;

    private T variable;
    public void complete(T variable) {

    }
}
