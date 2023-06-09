package io.rapidw.easybpmn.engine.repository;

import com.querydsl.jpa.impl.JPAQuery;
import io.rapidw.easybpmn.engine.runtime.ProcessInstance;
import io.rapidw.easybpmn.engine.runtime.QProcessInstance;
import jakarta.persistence.EntityManager;
import lombok.val;

public class ProcessInstanceRepository extends AbstractRepository<ProcessInstance> {

    public ProcessInstance get(EntityManager entityManager, Integer id) {
        val q = QProcessInstance.processInstance;
        val jpaQuery = new JPAQuery<ProcessInstance>(entityManager);
        return jpaQuery.from(q).where(q.id.eq(id)).fetchOne();
    }
}
