package io.rapidw.easybpmn.engine.operation;

import io.rapidw.easybpmn.engine.model.FlowNode;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@SuperBuilder
@Slf4j
public class LeaveFlowElementOperation extends AbstractOperation {

    @Override
    public List<AbstractOperation> execute() {
        log.debug("leave flow element {}", currentFlowElement.getId());
        if (currentFlowElement instanceof FlowNode flowNode) {
            return flowNode.getFlowNodeBehavior().onLeave(this.execution);
//        } else if (currentFlowElement instanceof SequenceFlow sequenceFlow) {
//            handleSequenceFlow(sequenceFlow);
//        }
        } else {
            notImplemented();
        }
        return null;
    }
}
