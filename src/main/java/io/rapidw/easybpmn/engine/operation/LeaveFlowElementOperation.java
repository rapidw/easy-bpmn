package io.rapidw.easybpmn.engine.operation;

import io.rapidw.easybpmn.engine.runtime.Execution;
import io.rapidw.easybpmn.engine.model.FlowNode;
import io.rapidw.easybpmn.engine.model.SequenceFlow;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.ArrayList;

@SuperBuilder
@Slf4j
public class LeaveFlowElementOperation extends AbstractOperation {

    @Override
    public void execute() {
        log.debug("current flow element {}", currentFlowElement.getId());
        if (currentFlowElement instanceof FlowNode flowNode) {
            handleFlowNode(flowNode);
        } else if (currentFlowElement instanceof SequenceFlow sequenceFlow) {
            handleSequenceFlow(sequenceFlow);
        }
    }

    private void handleFlowNode(FlowNode flowNode) {
        val outgoingSequenceFlows = flowNode.getOutgoing();
        if (!outgoingSequenceFlows.isEmpty()) {
            val outgoingExecutions = new ArrayList<Execution>(outgoingSequenceFlows.size());

            //reuse current execution
            execution.setCurrentFlowElementId(outgoingSequenceFlows.get(0).getId());
            outgoingExecutions.add(execution);

            if (outgoingSequenceFlows.size() > 1) {
                for (int i = 1; i < outgoingSequenceFlows.size(); i++) {
                    val sf = outgoingSequenceFlows.get(i);
                    val parent = execution.getParent() != null ? execution.getParent() : execution;

                    val child = Execution.builder()
                        .processInstance(execution.getProcessInstance())
                        .parent(parent)
                        .initialFlowElement(sf)
                        .active(true)
                        .variable(execution.getVariable())
                        .build();
                    this.execution.getChildren().add(child);
                    outgoingExecutions.add(child);
                }
            }
            outgoingExecutions.forEach(this::planContinueProcessOperation);
        } else {
            log.debug("execution finished");
        }
    }

    private void handleSequenceFlow(SequenceFlow sequenceFlow) {
        planContinueProcessOperation(this.execution);
    }
}
