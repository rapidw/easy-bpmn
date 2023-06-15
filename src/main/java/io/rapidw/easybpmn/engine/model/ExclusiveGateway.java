package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.Execution;
import io.rapidw.easybpmn.engine.operation.ContinueProcessEngineOperation;
import jakarta.el.StandardELContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.val;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExclusiveGateway extends Gateway {

    private SequenceFlow defaultFlow;


    public class ExclusiveGatewayBehavior implements Behavior {

        @Override
        public void execute(Execution execution) {
            // get all outgoing sequence flows
            SequenceFlow targetFlow = null;
            for (SequenceFlow sequenceFlow : getOutgoing()) {
                if (evaluateCondition(execution, sequenceFlow.getConditionExpression())) {
                    targetFlow = sequenceFlow;
                    break;
                }
            }
            if (targetFlow == null && defaultFlow != null) {
                targetFlow = defaultFlow;
            }
            if (targetFlow == null ) {
                throw new ProcessEngineException("no outgoing sequence flow found in exclusive gateway");
            }
            execution.setCurrentFlowElementId(targetFlow.getTargetRef().getId());
            execution.merge();

            execution.getProcessInstance().getProcessEngine().addOperation(
                ContinueProcessEngineOperation.builder().executionId(execution.getId()).build());
        }

        @SneakyThrows
        private boolean evaluateCondition(Execution execution, String conditionExpression) {
            val  context = new StandardELContext(execution.getProcessInstance().getProcessEngine().getExpressionFactory());
            context.addELResolver(DEFAULT_READ_ONLY_RESOLVER);
            val clazz = Class.forName(execution.getProcessInstance().getVariable().getClazz());
            val variable = execution.getProcessInstance().getProcessEngine().getObjectMapper().readValue(execution.getProcessInstance().getVariable().getJson(), clazz);
            context.putContext(clazz, variable);
            return  execution.getProcessInstance().getProcessEngine().getExpressionFactory().createValueExpression(context, conditionExpression, Boolean.class).getValue(context);
        }
    }
}
