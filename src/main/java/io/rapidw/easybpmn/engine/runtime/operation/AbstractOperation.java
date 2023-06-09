package io.rapidw.easybpmn.engine.runtime.operation;

import io.rapidw.easybpmn.ProcessEngine;
import io.rapidw.easybpmn.engine.model.FlowElement;
import io.rapidw.easybpmn.engine.runtime.Execution;
import io.rapidw.easybpmn.engine.runtime.ProcessDefinition;
import io.rapidw.easybpmn.engine.runtime.ProcessInstance;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractOperation {

    private final Integer processDefinitionId;
    private final Integer processInstanceId;
    private final Integer executionId;
    protected ProcessEngine processEngine;
    protected EntityManager entityManager;
    protected ProcessDefinition processDefinition;
    protected ProcessInstance processInstance;
    protected Execution execution;
    protected FlowElement currentFlowElement;

    protected AbstractOperation(AbstractOperationBuilder<?, ?> b) {
        this.processDefinitionId = b.processDefinitionId;
        this.processInstanceId = b.processInstanceId;
        this.executionId = b.executionId;
    }

    public void execute(ProcessEngine processEngine, ThreadLocal<EntityManager> entityManagerThreadLocal) {
        this.processEngine = processEngine;
        this.entityManager = entityManagerThreadLocal.get();
        this.processDefinition = getProcessDefinition();
        this.processInstance = getProcessInstance();
        this.execution = getExecution();
        this.currentFlowElement = this.processDefinition.getProcess().getFlowElementMap().get(this.execution.getCurrentFlowElementId());
        log.debug("execute operation: {}, current flow element {}", this.getClass().getSimpleName(), this.execution.getCurrentFlowElementId());
        execute();
    }

    public abstract void execute();

    protected AbstractOperation(Integer processDefinitionId, Integer processInstanceId, Integer executionId) {
        this.processDefinitionId = processDefinitionId;
        this.processInstanceId = processInstanceId;
        this.executionId = executionId;
    }

    protected void planTakeOutgoingSequenceFlowsOperation(Execution execution) {
        planOperation(TakeOutgoingSequenceFlowOperation.builder()
            .processDefinitionId(processDefinitionId)
            .processInstanceId(processInstanceId)
            .executionId(execution.getId())
            .build());
    }

    protected void planContinueProcessOperation(Execution execution) {
        planOperation(ContinueProcessOperation.builder()
            .processDefinitionId(processDefinitionId)
            .processInstanceId(processInstanceId)
            .executionId(execution.getId())
            .build());
    }

    private void planOperation(AbstractOperation operation) {
        processEngine.addOperation(operation);
    }

    protected ProcessDefinition getProcessDefinition() {
        return this.processEngine.getProcessDefinitionService().get(this.processDefinitionId);
    }

    protected ProcessInstance getProcessInstance() {
        return this.processEngine.getProcessInstanceRepository().get(this.entityManager, this.processInstanceId);
    }

    protected Execution getExecution() {
        return this.processEngine.getExecutionRepository().get(this.entityManager, this.processInstanceId, this.executionId);
    }

    public static abstract class AbstractOperationBuilder<C extends AbstractOperation, B extends AbstractOperationBuilder<C, B>> {
        private Integer processDefinitionId;
        private Integer processInstanceId;
        private Integer executionId;

        public B processDefinitionId(Integer processDefinitionId) {

            this.processDefinitionId = processDefinitionId;
            return self();
        }

        public B processInstanceId(Integer processInstanceId) {
            this.processInstanceId = processInstanceId;
            return self();
        }

        public B executionId(Integer executionId) {
            this.executionId = executionId;
            return self();
        }

        protected abstract B self();

        public abstract C build();

        public String toString() {
            return "AbstractOperation.AbstractOperationBuilder(processDefinitionId=" + this.processDefinitionId + ", processInstanceId=" + this.processInstanceId + ", executionId=" + this.executionId + ")";
        }
    }
}
