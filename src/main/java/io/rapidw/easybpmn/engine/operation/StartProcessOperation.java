package io.rapidw.easybpmn.engine.operation;

import io.rapidw.easybpmn.engine.runtime.Execution;
import io.rapidw.easybpmn.engine.runtime.ProcessInstance;
import io.rapidw.easybpmn.engine.runtime.Variable;
import lombok.experimental.SuperBuilder;
import lombok.val;

import java.util.List;

@SuperBuilder
public class StartProcessOperation extends AbstractOperation {

    private final Long deploymentId;
    private final Object variable;

    protected StartProcessOperation(Long deploymentId, Object variable) {
        super((Long) null);
        this.deploymentId = deploymentId;
        this.variable = variable;
    }

    @Override
    public List<AbstractOperation> execute() {
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
            .variable(variable)
            .build();

        this.processEngine.getExecutionRepository().persist(execution);
        processInstance.getExecutions().add(execution);
//        this.processEngine.getProcessInstanceRepository().merge(processInstance);

        return List.of(EnterFlowElementOperation.builder()
            .executionId(execution.getId())
            .build()
        );
    }
}
