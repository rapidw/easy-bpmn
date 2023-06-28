package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.common.ExpressionType;
import io.rapidw.easybpmn.engine.operation.AbstractOperation;
import io.rapidw.easybpmn.engine.runtime.Execution;
import io.rapidw.easybpmn.engine.runtime.Variable;
import io.rapidw.easybpmn.utils.ElUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.List;

@Getter
@Setter
public class ServiceTask extends Task {

    private String implementation;
    private Operation operationRef;

    private String expression;
    private String delegateExpression;
    // todo: support groovy
    private ExpressionType expressionType;
    private String javaClass;

    public class ServiceTaskBehavior extends FlowNodeBehavior {

        @Override
        public List<AbstractOperation> onEnter() {
            var object = execution.getVariable().deserialize(execution.getProcessEngine().getObjectMapper());
            val objectDup = execution.getVariable().deserialize(execution.getProcessEngine().getObjectMapper());
            ElUtils.evaluateCondition(execution, expression, object, Object.class);
            if (!objectDup.equals(object)) {
                val variable = new Variable(execution.getProcessEngine().getObjectMapper(), object);

                val child = Execution.builder()
                    .processInstance(execution.getProcessInstance())
                    .parent(execution)
                    .initialFlowElement(ServiceTask.this)
                    .active(true)
                    .variable(variable)
                    .build();
                execution.getProcessEngine().getExecutionRepository().persist(child);
                execution.getChildren().add(child);
                execution.getProcessInstance().setVariable(variable);
                execution.setActive(false);
                child.setProcessEngine(execution.getProcessEngine());
                return planLeave(child);
            } else {
                return planLeave(execution);
            }
        }

        @Override
        protected List<AbstractOperation> onLeave() {
            return doLeave();
        }
    }
}
