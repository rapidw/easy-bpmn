package io.rapidw.easybpmn.task;

import io.rapidw.easybpmn.engine.runtime.ProcessInstance;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class TaskQuery {

    private Integer id;
    private String assignee;
    private ProcessInstance processInstance;
}
