package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.Execution;
import io.rapidw.easybpmn.engine.TaskInstance;
import lombok.val;

public class UserTask extends Task {

    // todo: support assignee
    public class UserTaskBehavior extends Behavior {

        @Override
        public void execute(Execution execution) {
            val taskInstance = TaskInstance.builder()
                .execution(execution)
                .userTaskId(getId())
                .build();
            execution.getProcessEngine().getTaskInstanceRepository().persist(taskInstance);
        }
    }
}
