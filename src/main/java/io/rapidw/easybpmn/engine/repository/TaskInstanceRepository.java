package io.rapidw.easybpmn.engine.repository;

import com.querydsl.jpa.impl.JPAQuery;
import io.rapidw.easybpmn.engine.runtime.QTaskInstance;
import io.rapidw.easybpmn.engine.runtime.TaskInstance;
import io.rapidw.easybpmn.task.TaskQuery;
import jakarta.persistence.EntityManager;
import lombok.val;

import java.util.List;

public class TaskInstanceRepository extends AbstractRepository<TaskInstance> {
    public TaskInstanceRepository(ThreadLocal<EntityManager> entityManagerThreadLocal) {
        super(entityManagerThreadLocal);
    }

    public List<TaskInstance> query(TaskQuery taskQuery) {
        JPAQuery<TaskInstance> executionJPAQuery = new JPAQuery<>(getEntityManager());
        val q = QTaskInstance.taskInstance;
        return executionJPAQuery.from(q).where(q.processInstance.id.eq(taskQuery.getId())).fetch();
    }
}
