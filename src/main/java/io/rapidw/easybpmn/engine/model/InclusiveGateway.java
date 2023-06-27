package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.ProcessEngineException;
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

    private SequenceFlow defaultFlow;

    public class InclusiveGatewayBehavior extends FlowElementBehavior {

        @Override
        public void onEnter(Execution execution) {
            // enter inclusive gateway: wait for all available incoming sequence flows to arrive
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
            //  leave inclusive gateway: continue sequence flow of true
            if (leave) {

                val variableObject = execution.getVariable().deserialize(execution.getProcessEngine().getObjectMapper());
                List<SequenceFlow> targetFlows = new ArrayList<>();
                for (SequenceFlow sequenceFlow : getOutgoing()) {
                    if (sequenceFlow.getConditionExpression() != null
                        && sequenceFlow.getExpressionType().evaluateToBoolean(execution, sequenceFlow.getConditionExpression(), variableObject)) {
                        targetFlows.add(sequenceFlow);
                    }
                    if (targetFlows.isEmpty()) {
                        if (getDefaultFlow() != null) {
                            targetFlows.add(getDefaultFlow());
                        } else {
                            throw new ProcessEngineException("no outgoing sequence flow found in inclusive gateway");
                        }
                    }

                    leave(execution, targetFlows);
                }
            }
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
