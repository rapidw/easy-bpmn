package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.runtime.Execution;
import io.rapidw.easybpmn.engine.runtime.TaskInstance;
import lombok.val;

public class UserTask extends Task {

    // todo: support assignee
    public class UserTaskBehavior extends FlowElementBehavior {

        @Override
        public void onEnter(Execution execution) {
            val taskInstance = TaskInstance.builder()
                .execution(execution)
                .userTaskId(getId())
                .build();
            execution.getProcessEngine().getTaskInstanceRepository().persist(taskInstance);
        }
    }
}
