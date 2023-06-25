package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.runtime.Execution;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Getter
@Setter
@Slf4j
public class ParallelGateway extends Gateway {

    public static class ParallelGatewayBehavior extends FlowElementBehavior {
        public final static ParallelGateway.ParallelGatewayBehavior INSTANCE = new ParallelGateway.ParallelGatewayBehavior();

        @Override
        public void onEnter(Execution execution) {
            // enter: wait for all incoming sequence flows
            log.debug("enter parallel gateway {} in process instance {}", execution.getCurrentFlowElementId(), execution.getProcessInstance().getId());
            val currentGateway = ((ParallelGateway) execution.getProcessEngine().getProcessDefinitionManager().get(execution.getProcessInstance().getDeploymentId())
                .getProcess().getFlowElementMap().get(execution.getCurrentFlowElementId()));

            boolean leave = true;
            for (SequenceFlow sequenceFlow : currentGateway.getIncoming()) {
                // if wait at current inclusive gateway
                if (execution.getProcessEngine().getExecutionRepository().getOneByProcessInstanceAndCurrentFlowElementId(execution.getProcessInstance(), sequenceFlow.getId()) == null) {
                    leave = false;
                    break;
                }
            }

            // leave: leave all outgoing sequence flows, ignore condition expression
            if (leave) {
                leave(execution, currentGateway.getOutgoing());
            }
        }
    }
}
