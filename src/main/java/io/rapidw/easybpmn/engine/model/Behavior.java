package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.Execution;
import io.rapidw.easybpmn.engine.operation.TakeOutgoingSequenceFlowEngineOperation;

public interface Behavior {

    default void execute(Execution execution) {
        leave(execution);
    }

    private void leave(Execution execution) {
        execution.getProcessInstance().getProcessEngine().addOperation(
            TakeOutgoingSequenceFlowEngineOperation.builder()
                .executionId(execution.getId())
                .build()
        );
    }
}
