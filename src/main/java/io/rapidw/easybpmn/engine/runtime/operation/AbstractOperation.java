package io.rapidw.easybpmn.engine.runtime.operation;

import io.rapidw.easybpmn.ProcessEngine;
import io.rapidw.easybpmn.engine.runtime.Execution;
import io.rapidw.easybpmn.engine.runtime.ProcessDefinition;
import io.rapidw.easybpmn.engine.runtime.ProcessInstance;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractOperation implements Operation {

    private Integer processDefinitionId;
    private Integer processInstanceId;
    private Integer executionId;
    protected ProcessEngine processEngine;
    protected ProcessDefinition processDefinition;
    protected ProcessInstance processInstance;
    protected Execution execution;

    protected AbstractOperation(AbstractOperationBuilder<?, ?> b) {
        this.processDefinitionId = b.processDefinitionId;
        this.processInstanceId = b.processInstanceId;
        this.executionId = b.executionId;
    }

    @Override
    public void execute(ProcessEngine processEngine) {
        this.processEngine = processEngine;
        this.processDefinition = getProcessDefinition();
        this.processInstance = getProcessInstance();
        this.execution = getExecution();
        this.execution.setCurrentFlowElement(this.processDefinition.getProcess().getFlowElementMap().get(this.execution.getCurrentFlowElement().getId()));
        log.debug("execute operation: {}, current flow element {}", this.getClass().getSimpleName(), this.execution.getCurrentFlowElement().getId());
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

    private void planOperation(Operation operation) {
        processEngine.addOperation(operation);
    }

    protected ProcessDefinition getProcessDefinition() {
        return this.processEngine.getProcessDefinitionService().get(this.processDefinitionId);
    }

    protected ProcessInstance getProcessInstance() {
        return this.processEngine.getProcessInstanceRepository().get(this.processInstanceId);
    }

    protected Execution getExecution() {
        return this.processEngine.getExecutionRepository().get(this.processInstanceId, this.executionId);
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
