package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.common.Candidate;
import io.rapidw.easybpmn.engine.operation.AbstractOperation;
import io.rapidw.easybpmn.engine.runtime.TaskCandidate;
import io.rapidw.easybpmn.engine.runtime.TaskInstance;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class UserTask extends Task {

    private String assignee;
    private List<Candidate> candidates;

    public class UserTaskBehavior extends FlowNodeBehavior {

        @Override
        public List<AbstractOperation> onEnter() {
            val taskCandidates = new ArrayList<TaskCandidate>(candidates.size());
            candidates.forEach(candidate -> {
                val taskCandidate = new TaskCandidate();
                taskCandidate.setCandidate(candidate);
                execution.getProcessEngine().getTaskCandidateRepository().persist(taskCandidate);
                taskCandidates.add(taskCandidate);
            });

            val taskInstance = TaskInstance.builder()
                .execution(execution)
                .userTaskId(getId())
                .assignee(assignee)
                .candidates(taskCandidates)
                .build();
            execution.getProcessEngine().getTaskInstanceRepository().persist(taskInstance);
            taskCandidates.forEach(taskCandidate -> taskCandidate.setTaskInstance(taskInstance));
            return Collections.emptyList();
        }

        @Override
        protected List<AbstractOperation> onLeave() {
            return doLeave();
        }
    }
}
