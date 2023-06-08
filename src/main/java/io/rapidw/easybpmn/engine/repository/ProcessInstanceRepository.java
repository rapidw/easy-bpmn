package io.rapidw.easybpmn.engine.repository;

import com.querydsl.jpa.impl.JPAQuery;
import io.rapidw.easybpmn.engine.runtime.ProcessInstance;
import io.rapidw.easybpmn.engine.runtime.QProcessInstance;
import jakarta.persistence.EntityManagerFactory;
import lombok.val;

public class ProcessInstanceRepository extends AbstractRepository<ProcessInstance> {
    public ProcessInstanceRepository(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public ProcessInstance get(Integer id) {
        val q = QProcessInstance.processInstance;
        val jpaQuery = new JPAQuery<ProcessInstance>(entityManagerFactory.createEntityManager());
        return jpaQuery.from(q).where(q.id.eq(id)).fetchOne();
    }
}
