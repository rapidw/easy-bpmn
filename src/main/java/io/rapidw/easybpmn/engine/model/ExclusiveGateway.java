package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.operation.EnterFlowElementOperation;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

@Getter
@Setter
public class ExclusiveGateway extends Gateway {

    private SequenceFlow defaultFlow;

    public class ExclusiveGatewayBehavior extends FlowNodeBehavior {

        @Override
        public void onEnter() {
            // get all outgoing sequence flows
            SequenceFlow targetFlow = null;
            for (SequenceFlow sequenceFlow : getOutgoing()) {
                val variableObject = execution.getVariable().deserialize(execution.getProcessEngine().getObjectMapper());
                if (sequenceFlow.getConditionExpression() != null && sequenceFlow.getExpressionType().evaluateToBoolean(execution, sequenceFlow.getConditionExpression(), variableObject)) {
                    targetFlow = sequenceFlow;
                    break;
                }
            }
            if (targetFlow == null && defaultFlow != null) {
                targetFlow = defaultFlow;
            }
            if (targetFlow == null) {
                throw new ProcessEngineException("no outgoing sequence flow found in exclusive gateway");
            }
            execution.setCurrentFlowElementId(targetFlow.getTargetRef().getId());
            execution.getProcessEngine().addOperation(
                EnterFlowElementOperation.builder().executionId(execution.getId()).build());
        }
    }
}
