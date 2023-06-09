package io.rapidw.easybpmn.task;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class TaskQuery {

    private Integer id;
    private String assignee;
    private Integer processInstanceId;
}
