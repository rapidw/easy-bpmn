package io.rapidw.easybpmn.engine.behavior;

import io.rapidw.easybpmn.engine.Execution;
import io.rapidw.easybpmn.engine.operation.TakeOutgoingSequenceFlowOperation;

public interface Behavior {

    default void execute(Execution execution) {
        leave(execution);
    }

    private void leave(Execution execution) {
        execution.getProcessInstance().getProcessEngine().addOperation(
            TakeOutgoingSequenceFlowOperation.builder()
                .executionId(execution.getId())
                .build()
        );
    }
}
