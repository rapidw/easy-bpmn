package io.rapidw.easybpmn.engine.operation;

import io.rapidw.easybpmn.engine.Execution;
import io.rapidw.easybpmn.engine.ProcessInstance;
import io.rapidw.easybpmn.engine.Variable;
import lombok.experimental.SuperBuilder;
import lombok.val;

@SuperBuilder
public class StartProcessEngineOperation extends AbstractEngineOperation {

    private final Integer deploymentId;
    private final Object variable;

    protected StartProcessEngineOperation(Integer deploymentId, Object variable) {
        super((Integer) null);
        this.deploymentId = deploymentId;
        this.variable = variable;
    }

    @Override
    public void execute() {
// save variable
        val variable = new Variable(processEngine.getObjectMapper(), this.variable);
        processEngine.getVariableRepository().persist(variable);

        // create process instance
        val processInstance = new ProcessInstance(deploymentId, variable);
        processEngine.getProcessInstanceRepository().persist(processInstance);
        val processDefinition = processEngine.getProcessDefinitionManager().get(deploymentId);

        val execution = Execution.builder()
            .processInstance(processInstance)
            .initialFlowElement(processDefinition.getProcess().getInitialFlowElement())
            .active(true)
            .parent(null)
            .variable(variable.duplicate())
            .build();

        this.processEngine.getExecutionRepository().persist(execution);
        processInstance.getExecutions().add(execution);
//        this.processEngine.getProcessInstanceRepository().merge(processInstance);

        processEngine.addOperation(ContinueProcessEngineOperation.builder()
            .executionId(execution.getId())
            .build()
        );
    }
}
