package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.runtime.Execution;
import io.rapidw.easybpmn.engine.operation.LeaveFlowElementOperation;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

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

    protected void leave(Execution currentExecution, List<SequenceFlow> sequenceFlows) {
        List<Execution> executions = new ArrayList<>(sequenceFlows.size());
        // reuse current execution
        currentExecution.setCurrentFlowElementId(sequenceFlows.get(0).getId());
        executions.add(currentExecution);


        for (int i = 1; i < sequenceFlows.size(); i++) {
            val sf = sequenceFlows.get(i);
            val child = Execution.builder()
                .processInstance(currentExecution.getProcessInstance())
                .parent(currentExecution)
                .initialFlowElement(sf)
                .active(true)
                .variable(currentExecution.getVariable())
                .build();
            executions.add(child);
        }
        executions.forEach(this::leave);
    }
}
