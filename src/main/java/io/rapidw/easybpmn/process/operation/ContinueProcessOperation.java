package io.rapidw.easybpmn.process.operation;


import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.model.Event;
import io.rapidw.easybpmn.model.FlowNode;
import io.rapidw.easybpmn.model.SequenceFlow;
import io.rapidw.easybpmn.model.StartEvent;
import lombok.experimental.SuperBuilder;
import lombok.val;

@SuperBuilder
public class ContinueProcessOperation extends AbstractOperation {

    @Override
    public void execute() {

        val currentFlowElement = execution.getCurrentFlowElement();
        if (currentFlowElement instanceof FlowNode flowNode) {
            handleFlowNode(flowNode);
        } else if (currentFlowElement instanceof SequenceFlow sequenceFlow) {
            handleSequenceFlow(sequenceFlow);
        } else {
            throw new ProcessEngineException("invalid flow element type:" + currentFlowElement.getClass().getName());
        }
    }

    private void handleFlowNode(FlowNode flowNode) {
        if (flowNode instanceof Event event) {
            if (event instanceof StartEvent startEvent) {
                planTakeOutgoingSequenceFlowsOperation();
            }
        }
    }

    private void handleSequenceFlow(SequenceFlow sequenceFlow) {

    }
}
