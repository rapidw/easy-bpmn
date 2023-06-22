package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.runtime.Execution;
import io.rapidw.easybpmn.engine.operation.LeaveFlowElementOperation;

public abstract class FlowElementBehavior {

    public void onEnter(Execution execution) {
        leave(execution);
    }

    protected void leave(Execution execution) {
        execution.getProcessEngine().addOperation(
            LeaveFlowElementOperation.builder()
                .executionId(execution.getId())
                .build()
        );
    }
}
