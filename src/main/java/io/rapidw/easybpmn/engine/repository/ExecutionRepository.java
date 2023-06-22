package io.rapidw.easybpmn.engine.repository;

import com.querydsl.jpa.impl.JPAQuery;
import io.rapidw.easybpmn.engine.runtime.*;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.List;

@Slf4j
public class ExecutionRepository extends AbstractRepository<Execution> {
    public ExecutionRepository(ThreadLocal<EntityManager> entityManagerThreadLocal) {
        super(entityManagerThreadLocal);
    }

    public List<Execution> getAllActiveExecutionByProcessInstance(ProcessInstance processInstance) {
        val query = new JPAQuery<Execution>(getEntityManager());
        val q = QExecution.execution;
        val res = query.from(q).where(
            q.processInstance.eq(processInstance),
            q.active.eq(true)
        ).fetch();
        log.debug("getAllActiveExecutionByProcessInstance, count {}", res.size());
        return res;
    }
}
