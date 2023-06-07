package io.rapidw.easybpmn.engine.service;

import com.querydsl.jpa.impl.JPAQuery;
import io.rapidw.easybpmn.engine.runtime.ProcessDefinition;
import io.rapidw.easybpmn.process.QProcessDefinition;
import jakarta.persistence.EntityManagerFactory;
import lombok.val;

public class ProcessDefinitionService extends AbstractRepositoryService<ProcessDefinition> {

    public ProcessDefinitionService(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public ProcessDefinition query(Integer id) {
        val q = QProcessDefinition.processDefinition;
        val query = new JPAQuery<ProcessDefinition>(this.entityManagerFactory.createEntityManager());
        query.from(q).where(q.id.eq(id).and());

    }
}
