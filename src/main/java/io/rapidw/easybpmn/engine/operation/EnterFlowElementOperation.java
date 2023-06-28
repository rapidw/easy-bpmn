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
        log.debug("enter flow element {}", execution.getCurrentFlowElementId());
        if (currentFlowElement instanceof FlowNode flowNode) {
            flowNode.getFlowNodeBehavior().onEnter(execution);
        } else if (currentFlowElement instanceof SequenceFlow sequenceFlow) {
            handleSequenceFlow(sequenceFlow);
        } else {
            throw new ProcessEngineException("invalid flow element " + currentFlowElement.getId() + " of type " + currentFlowElement.getClass().getSimpleName());
        }
    }

    private void handleFlowNode(FlowNode flowNode) {

    }


    private void handleSequenceFlow(SequenceFlow sequenceFlow) {
        this.execution.setCurrentFlowElementId(sequenceFlow.getTargetRef().getId());
        planContinueProcessOperation(this.execution);
    }

}
