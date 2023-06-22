package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.runtime.Execution;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Slf4j
public class InclusiveGateway extends Gateway {

    public static class InclusiveGatewayBehavior extends FlowElementBehavior {
        public static final InclusiveGateway.InclusiveGatewayBehavior INSTANCE = new InclusiveGateway.InclusiveGatewayBehavior();

        @Override
        public void onEnter(Execution execution) {
            log.debug("enter inclusive gateway {} in process instance {}", execution.getCurrentFlowElementId(), execution.getProcessInstance().getId());
            val activeExecutions = execution.getProcessEngine().getExecutionRepository().getAllActiveExecutionByProcessInstance(execution.getProcessInstance());
            boolean leave = true;
            for (val activeExecution : activeExecutions) {
                if (activeExecution.getId().equals(execution.getId())) {
                    continue;
                }
                if (mayHavePathToCurrentElement(execution.getCurrentFlowElementId(), activeExecution)) {
                    leave = false;
                    break;
                }
            }
            log.debug("leave {}", leave);
            // todo: leave inclusive gateway
            if (leave) leave(execution);
        }

        private boolean mayHavePathToCurrentElement(String currentFlowElementId, Execution execution) {
            // transverse the execution tree to see if there is a path to the current element
            val start = execution.getProcessEngine().getProcessDefinitionManager().get(execution.getProcessInstance().getDeploymentId()).getProcess().getFlowElementMap()
                .get(execution.getCurrentFlowElementId());

            List<FlowElement> toVisit = new ArrayList<>();
            toVisit.add(start);
            while (!toVisit.isEmpty()) {
                val current = toVisit.remove(0);
                if (current.getId().equals(currentFlowElementId)) {
                    return true;
                }
                if (current instanceof Gateway gateway) {
                    for (val outgoingSequenceFlow : gateway.getOutgoing()) {
                        toVisit.add(outgoingSequenceFlow.getTargetRef());
                    }
                } else if (current instanceof Activity activity) {
                    for (val outgoingSequenceFlow : activity.getOutgoing()) {
                        toVisit.add(outgoingSequenceFlow.getTargetRef());
                    }
                } else if (current instanceof Event event) {
                    for (val outgoingSequenceFlow : event.getOutgoing()) {
                        toVisit.add(outgoingSequenceFlow.getTargetRef());
                    }
                }
            }
            return false;
        }
    }
}
