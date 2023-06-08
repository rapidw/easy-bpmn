package io.rapidw.easybpmn.engine.repository;

import com.querydsl.jpa.impl.JPAQuery;
import io.rapidw.easybpmn.engine.runtime.TaskInstance;
import io.rapidw.easybpmn.task.TaskQuery;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class TaskRepository extends AbstractRepository<TaskInstance> {

    public TaskRepository(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public List<TaskInstance> queryTask(TaskQuery taskQuery) {
        JPAQuery<TaskInstance> executionJPAQuery = new JPAQuery<>(this.entityManagerFactory.createEntityManager());
//         qExecution = QExecution.execution;
//        return executionJPAQuery.from(qExecution).where(qExecution.processInstance.id.eq(processInstanceId).and(qExecution.id.eq(executionId))).fetchOne();
        return null;
    }
}
