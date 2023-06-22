package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.runtime.Execution;
import io.rapidw.easybpmn.engine.runtime.Variable;
import io.rapidw.easybpmn.utils.ElUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

@Getter
@Setter
public class ServiceTask extends Task {

    private String implementation;
    private Operation operationRef;

    private String expression;

    public class ServiceTaskBehavior extends FlowElementBehavior {

        @Override
        public void onEnter(Execution execution) {
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
                leave(child);
            } else {
                leave(execution);
            }
        }
    }
}
