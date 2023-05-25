package io.rapidw.easybpmn.process;

import io.rapidw.easybpmn.process.model.FlowElement;
import io.rapidw.easybpmn.service.TaskService;
import io.rapidw.easybpmn.task.TaskQuery;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
public class Execution {
//    private Integer id;
    @ToString.Exclude
    private final ProcessInstance<?> processInstance;
    private TaskService taskService;
    private final FlowElement currentFlowElement;

    @Builder
    public Execution(ProcessInstance<?> processInstance, FlowElement initialFlowElement) {
        this.processInstance = processInstance;
        this.taskService = processInstance.getProcessDefinition().getProcessEngine().getTaskService();
        this.currentFlowElement = initialFlowElement;
    }

    public TaskInstance<?, ?> queryTask(TaskQuery taskQuery) {
        return null;
    }
}
