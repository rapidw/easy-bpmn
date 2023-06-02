package io.rapidw.easybpmn.process.operation;

import com.querydsl.jpa.impl.JPAQuery;
import io.rapidw.easybpmn.ProcessEngine;
import io.rapidw.easybpmn.process.Execution;
import io.rapidw.easybpmn.process.ProcessDefinition;
import io.rapidw.easybpmn.process.ProcessInstance;
import io.rapidw.easybpmn.process.QExecution;
import jakarta.persistence.EntityManager;

public abstract class AbstractOperation implements Operation {

    private Integer processDefinitionId;
    private Integer processInstanceId;
    private Integer executionId;
    protected ProcessEngine processEngine;
    protected EntityManager entityManager;
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
        this.entityManager = processEngine.getEntityManagerFactory().createEntityManager();
        this.processDefinition = getProcessDefinition();
        this.processInstance = getProcessInstance();
        this.execution = getExecution();
        execute();
        this.entityManager.close();
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
            .executionId(executionId)
            .build());
    }

    protected void planContinueProcessOperation() {
        planOperation(ContinueProcessOperation.builder()
            .processDefinitionId(processDefinitionId)
            .processInstanceId(processInstanceId)
            .executionId(executionId)
            .build());
    }

    protected void planTakeOutgoingSequenceFlowsOperation() {
        planTakeOutgoingSequenceFlowsOperation(execution);
    }

    private void planOperation(Operation operation) {
        processEngine.addOperation(operation);
    }

    protected ProcessDefinition getProcessDefinition() {
        return null;
    }

    protected ProcessInstance getProcessInstance() {
        return null;
    }

    protected Execution getExecution() {
        JPAQuery<Execution> executionJPAQuery = new JPAQuery<>(this.entityManager);
        QExecution qExecution = QExecution.execution;
        return executionJPAQuery.from(qExecution).where(qExecution.processInstance.id.eq(this.processInstanceId)).fetchOne();
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
