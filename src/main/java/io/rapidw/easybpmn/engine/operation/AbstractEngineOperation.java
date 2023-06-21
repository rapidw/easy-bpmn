package io.rapidw.easybpmn.engine.operation;

import io.rapidw.easybpmn.engine.ProcessEngine;
import io.rapidw.easybpmn.engine.model.FlowElement;
import io.rapidw.easybpmn.engine.Execution;
import io.rapidw.easybpmn.engine.ProcessDefinition;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuperBuilder
public abstract class AbstractEngineOperation {

    private final Integer executionId;

    protected ProcessEngine processEngine;
    protected Execution execution;
    protected ProcessDefinition processDefinition;
    protected FlowElement currentFlowElement;
    private EntityTransaction transaction;

    public void execute(ProcessEngine processEngine) {
        this.transaction = getEntityManager(processEngine).getTransaction();
        this.transaction.begin();
        try {
            this.processEngine = processEngine;
            log.debug("operation execution id: {}", this.executionId);
            this.execution = this.processEngine.getExecutionRepository().get(this.executionId);
            this.processDefinition = processEngine.getProcessDefinitionManager().get(execution.getProcessInstance().getDeploymentId());
            this.currentFlowElement = this.processDefinition.getProcess().getFlowElementMap().get(this.execution.getCurrentFlowElementId());
            log.debug("execute operation: {}, current flow element {}", this.getClass().getSimpleName(), this.execution.getCurrentFlowElementId());
            execute();
            this.transaction.commit();
        } catch (Exception e) {
            this.transaction.rollback();
            throw e;
        }
    }

    public abstract void execute();

    protected AbstractEngineOperation(Integer executionId) {
        this.executionId = executionId;
    }

    protected void planTakeOutgoingSequenceFlowsOperation(Execution execution) {
        planOperation(TakeOutgoingSequenceFlowEngineOperation.builder()
            .executionId(execution.getId())
            .build());
    }

    protected void planContinueProcessOperation(Execution execution) {
        planOperation(ContinueProcessEngineOperation.builder()
            .executionId(execution.getId())
            .build());
    }

    private void planOperation(AbstractEngineOperation operation) {
        processEngine.addOperation(operation);
    }

    private EntityManager getEntityManager(ProcessEngine processEngine) {
        return processEngine.getEntityManagerThreadLocal().get();
    }
}
