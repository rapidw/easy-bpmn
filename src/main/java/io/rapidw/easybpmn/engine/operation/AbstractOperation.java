package io.rapidw.easybpmn.engine.operation;

import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.ProcessEngine;
import io.rapidw.easybpmn.engine.model.FlowElement;
import io.rapidw.easybpmn.engine.runtime.Execution;
import io.rapidw.easybpmn.utils.TransactionUtils;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@SuperBuilder
public abstract class AbstractOperation {

    private final Long executionId;

    protected ProcessEngine processEngine;
    protected Execution execution;
    protected FlowElement currentFlowElement;

    public void execute(ProcessEngine processEngine) {
        List<AbstractOperation> nextOperations = TransactionUtils.callWithTransaction(processEngine,() -> {
            this.processEngine = processEngine;
            this.execution = this.processEngine.getExecutionRepository().get(this.executionId);
            this.currentFlowElement = this.processEngine.getProcessDefinitionManager().get(this.execution.getProcessInstance().getDeploymentId()).getProcess().getFlowElementMap().get(this.execution.getCurrentFlowElementId());
            log.debug("execute operation {}, current flow element {}, execution id {}", this.getClass().getSimpleName(), this.execution.getCurrentFlowElementId(), this.executionId);
            return execute();
        });
        if (nextOperations != null) {
            nextOperations.forEach(processEngine::addOperation);
        } else {
            log.error("operation list is null");
        }
    }

    public abstract List<AbstractOperation> execute();

    protected AbstractOperation(Long executionId) {
        this.executionId = executionId;
    }

    protected void notImplemented() {
        throw new ProcessEngineException("invalid flow node " + execution.getCurrentFlowElementId());
    }
}
