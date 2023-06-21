package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.Execution;
import io.rapidw.easybpmn.engine.operation.TakeOutgoingSequenceFlowEngineOperation;

public abstract class Behavior {

    public void execute(Execution execution) {
        leave(execution);
    }

    protected void leave(Execution execution) {
        execution.getProcessEngine().addOperation(
            TakeOutgoingSequenceFlowEngineOperation.builder()
                .executionId(execution.getId())
                .build()
        );
    }
}
