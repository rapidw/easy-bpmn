package io.rapidw.easybpmn.engine.operation;


import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.model.FlowNode;
import io.rapidw.easybpmn.engine.model.SequenceFlow;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@SuperBuilder
@Slf4j
public class EnterFlowElementOperation extends AbstractOperation {

    @Override
    public List<AbstractOperation> execute() {
        log.debug("enter flow element {}", execution.getCurrentFlowElementId());
        if (currentFlowElement instanceof FlowNode flowNode) {
            return flowNode.getFlowNodeBehavior().onEnter(execution);
        } else if (currentFlowElement instanceof SequenceFlow sequenceFlow) {
            return handleSequenceFlow(sequenceFlow);
        } else {
            throw new ProcessEngineException("invalid flow element " + currentFlowElement.getId() + " of type " + currentFlowElement.getClass().getSimpleName());
        }
    }

    private List<AbstractOperation> handleSequenceFlow(SequenceFlow sequenceFlow) {
        this.execution.setCurrentFlowElementId(sequenceFlow.getTargetRef().getId());
        return List.of(EnterFlowElementOperation.builder()
            .executionId(execution.getId())
            .build());
    }

}
