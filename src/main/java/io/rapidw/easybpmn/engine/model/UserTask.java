package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.runtime.Execution;
import io.rapidw.easybpmn.engine.runtime.TaskCandidate;
import io.rapidw.easybpmn.engine.runtime.TaskInstance;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserTask extends Task {

    private String assignee;
    private List<TaskCandidate.Candidate> candidates;

    public class UserTaskBehavior extends FlowElementBehavior {

        @Override
        public void onEnter(Execution execution) {
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
        }
    }
}
