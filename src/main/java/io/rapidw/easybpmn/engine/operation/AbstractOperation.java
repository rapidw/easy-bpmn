package io.rapidw.easybpmn.engine.operation;

import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.ProcessEngine;
import io.rapidw.easybpmn.engine.model.FlowElement;
import io.rapidw.easybpmn.engine.runtime.Execution;
import io.rapidw.easybpmn.utils.TransactionUtils;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuperBuilder
public abstract class AbstractOperation {

    private final Long executionId;

    protected ProcessEngine processEngine;
    protected Execution execution;
    protected FlowElement currentFlowElement;

    public void execute(ProcessEngine processEngine) {
        TransactionUtils.callWithTransaction(processEngine,() -> {
            this.processEngine = processEngine;
            this.execution = this.processEngine.getExecutionRepository().get(this.executionId);
            this.currentFlowElement = this.processEngine.getProcessDefinitionManager().get(this.execution.getProcessInstance().getDeploymentId()).getProcess().getFlowElementMap().get(this.execution.getCurrentFlowElementId());
            log.debug("execute operation {}, current flow element {}, execution id {}", this.getClass().getSimpleName(), this.execution.getCurrentFlowElementId(), this.executionId);
            execute();
            return null;
        });
    }

    public abstract void execute();

    protected AbstractOperation(Long executionId) {
        this.executionId = executionId;
    }

    protected void notImplemented() {
        throw new ProcessEngineException("invalid flow node " + execution.getCurrentFlowElementId());
    }

    protected void planLeaveFlowElementOperation(Execution execution) {
        planOperation(LeaveFlowElementOperation.builder()
            .executionId(execution.getId())
            .build());
    }

    protected void planContinueProcessOperation(Execution execution) {
        planOperation(EnterFlowElementOperation.builder()
            .executionId(execution.getId())
            .build());
    }

    private void planOperation(AbstractOperation operation) {
        processEngine.addOperation(operation);
    }
}
