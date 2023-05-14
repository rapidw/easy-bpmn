package io.rapidw.easybpmn.process;

import io.rapidw.easybpmn.task.Task;
import lombok.Data;

@Data
public class ProcessInstance {
    private Integer id;
    private Integer processDefinitionId;

    private Integer activityId;
    private Task task;

    public Task getCurrentTask() {
        return task;
    }
}
