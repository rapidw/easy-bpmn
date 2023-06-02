package io.rapidw.easybpmn.process.operation;

import io.rapidw.easybpmn.process.Execution;
import io.rapidw.easybpmn.model.FlowNode;
import io.rapidw.easybpmn.model.SequenceFlow;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.ArrayList;

@SuperBuilder
@Slf4j
public class TakeOutgoingSequenceFlowOperation extends AbstractOperation {

    @Override
    public void execute() {
        val currentFlowElement = execution.getCurrentFlowElement();

        log.debug("current flow element {}", currentFlowElement.getName());
        if (currentFlowElement instanceof FlowNode flowNode) {
            handleFlowNode(flowNode);
        } else if (currentFlowElement instanceof SequenceFlow sequenceFlow) {
            handleSequenceFlow(sequenceFlow);
        }
    }

    private void handleFlowNode(FlowNode flowNode) {
//        if (flowNode instanceof Activity activity) {
//            handleActivity(activity);
//        } else if (flowNode instanceof Gateway gateway) {
//            handleGateway(gateway);
//        } else if (flowNode instanceof Event event) {
//            handleSequenceFlow();
//        }
//        log.debug("current");
        val outgoingSequenceFlows = flowNode.getOutgoing();
        val outgoingExecutions = new ArrayList<Execution>(outgoingSequenceFlows.size());

        //reuse first execution
        execution.setCurrentFlowElement(outgoingSequenceFlows.get(0));
        execution.setActive(false);
        outgoingExecutions.add(execution);

        if (outgoingSequenceFlows.size() > 1) {
            for (int i = 1; i < outgoingSequenceFlows.size(); i++) {
                val sf = outgoingSequenceFlows.get(i);
                val parent = execution.getParent() != null ? execution.getParent() : execution;

                val child = Execution.builder().processInstance(processInstance)
                    .parent(parent)
                    .initialFlowElement(sf)
                    .active(false)
                    .build();
                parent.addChild(child);
                outgoingExecutions.add(child);
            }
        }

        outgoingExecutions.forEach(this::planTakeOutgoingSequenceFlowsOperation);
    }

    private void handleSequenceFlow(SequenceFlow sequenceFlow) {
        planContinueProcessOperation();
    }
}
