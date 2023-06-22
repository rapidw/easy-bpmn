package io.rapidw.easybpmn.engine.operation;


import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.model.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@SuperBuilder
@Slf4j
public class EnterFlowElementOperation extends AbstractOperation {

    @Override
    public void execute() {
        if (currentFlowElement instanceof FlowNode flowNode) {
            handleFlowNode(flowNode);
        } else if (currentFlowElement instanceof SequenceFlow sequenceFlow) {
            handleSequenceFlow(sequenceFlow);
        } else {
            throw new ProcessEngineException("invalid flow element " + currentFlowElement.getId() + " of type " + currentFlowElement.getClass().getSimpleName());
        }
    }

    private void handleFlowNode(FlowNode flowNode) {
        flowNode.getFlowElementBehavior().onEnter(execution);
    }

    private void notImplemented() {
        throw new ProcessEngineException("invalid flow node " + currentFlowElement.getId() + " of type" + currentFlowElement.getClass().getSimpleName());
    }

    private void handleSequenceFlow(SequenceFlow sequenceFlow) {
        this.execution.setCurrentFlowElementId(sequenceFlow.getTargetRef().getId());
        planContinueProcessOperation(this.execution);
    }

}
