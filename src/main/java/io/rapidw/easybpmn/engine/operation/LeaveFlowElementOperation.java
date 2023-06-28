package io.rapidw.easybpmn.engine.operation;

import io.rapidw.easybpmn.engine.model.FlowNode;
import io.rapidw.easybpmn.engine.model.SequenceFlow;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@SuperBuilder
@Slf4j
public class LeaveFlowElementOperation extends AbstractOperation {

    @Override
    public void execute() {
        log.debug("leave flow element {}", currentFlowElement.getId());
        if (currentFlowElement instanceof FlowNode flowNode) {
            flowNode.getFlowNodeBehavior().onLeave(this.execution);
//        } else if (currentFlowElement instanceof SequenceFlow sequenceFlow) {
//            handleSequenceFlow(sequenceFlow);
//        }
        } else {
            notImplemented();
        }
    }

    private void handleSequenceFlow(SequenceFlow sequenceFlow) {
        planContinueProcessOperation(this.execution);
    }
}
