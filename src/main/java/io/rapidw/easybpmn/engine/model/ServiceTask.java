package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.Execution;
import io.rapidw.easybpmn.engine.Variable;
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

    public class ServiceTaskBehavior extends Behavior {

        @Override
        public void execute(Execution execution) {
            var object = execution.getVariable().deserialize(execution.getProcessEngine().getObjectMapper());
            val objectDup = execution.getVariable().deserialize(execution.getProcessEngine().getObjectMapper());
            ElUtils.evaluateCondition(execution, expression, object, Object.class);
            if (!objectDup.equals(object)) {
                val variable = new Variable(execution.getProcessEngine().getObjectMapper(), object);

                val newExecution = Execution.builder()
                    .processInstance(execution.getProcessInstance())
                    .parent(execution)
                    .initialFlowElement(ServiceTask.this)
                    .active(true)
                    .variable(variable)
                    .build();
                execution.getProcessEngine().getExecutionRepository().persist(newExecution);
                execution.getChildren().add(newExecution);

                execution.getProcessInstance().setVariable(variable);
                newExecution.setProcessEngine(execution.getProcessEngine());
                leave(newExecution);
            } else {
                leave(execution);
            }
        }
    }
}
