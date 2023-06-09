package io.rapidw.easybpmn.engine.repository;

import com.querydsl.jpa.impl.JPAQuery;
import io.rapidw.easybpmn.engine.runtime.Execution;
import io.rapidw.easybpmn.engine.runtime.QExecution;
import jakarta.persistence.EntityManager;

public class ExecutionRepository extends AbstractRepository<Execution> {

    public Execution get(EntityManager entityManager, Integer processInstanceId, Integer executionId) {
        JPAQuery<Execution> executionJPAQuery = new JPAQuery<>(entityManager);
        QExecution qExecution = QExecution.execution;
        return executionJPAQuery.from(qExecution).where(qExecution.processInstance.id.eq(processInstanceId).and(qExecution.id.eq(executionId))).fetchOne();
    }
}
