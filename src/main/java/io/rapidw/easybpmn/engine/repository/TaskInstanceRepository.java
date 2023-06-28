package io.rapidw.easybpmn.engine.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import io.rapidw.easybpmn.engine.runtime.QTaskInstance;
import io.rapidw.easybpmn.engine.runtime.TaskInstance;
import io.rapidw.easybpmn.query.TaskInstanceQuery;
import jakarta.persistence.EntityManager;
import lombok.val;

import java.util.List;

public class TaskInstanceRepository extends AbstractRepository<TaskInstance> {
    public TaskInstanceRepository(ThreadLocal<EntityManager> entityManagerThreadLocal) {
        super(entityManagerThreadLocal);
    }

    public List<TaskInstance> query(TaskInstanceQuery taskInstanceQuery) {
        val entityManager = getEntityManager();
        JPAQuery<TaskInstance> executionJPAQuery = new JPAQuery<>(entityManager);
        val q = QTaskInstance.taskInstance;
        val builder = new BooleanBuilder();
        if (taskInstanceQuery.getId() != null) {
            builder.and(q.id.eq(taskInstanceQuery.getId()));
        }
        if (taskInstanceQuery.getProcessInstanceId() != null) {
            builder.and(q.processInstance.id.eq(taskInstanceQuery.getProcessInstanceId()));
        }
        if (taskInstanceQuery.getCandidates() != null) {
            for (val assignment : taskInstanceQuery.getCandidates()) {
                builder.and(q.candidates.any().candidate.eq(assignment));
            }
        }
        return executionJPAQuery.from(q).where(builder).fetch().stream().peek(entityManager::refresh).toList();
    }
}
