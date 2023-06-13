package io.rapidw.easybpmn.engine.repository;

import com.querydsl.jpa.impl.JPAQuery;
import io.rapidw.easybpmn.engine.Execution;
import io.rapidw.easybpmn.engine.QExecution;
import jakarta.persistence.EntityManager;

public class ExecutionRepository extends AbstractRepository<Execution> {
    public ExecutionRepository(ThreadLocal<EntityManager> entityManagerThreadLocal) {
        super(entityManagerThreadLocal);
    }

    public Execution get(Integer executionId) {
        JPAQuery<Execution> executionJPAQuery = new JPAQuery<>(getEntityManager());
        QExecution qExecution = QExecution.execution;
        return executionJPAQuery.from(qExecution).where(qExecution.id.eq(executionId)).fetchOne();
    }
}
