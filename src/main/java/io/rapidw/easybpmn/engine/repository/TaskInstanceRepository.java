package io.rapidw.easybpmn.engine.repository;

import com.querydsl.jpa.impl.JPAQuery;
import io.rapidw.easybpmn.engine.QTaskInstance;
import io.rapidw.easybpmn.engine.TaskInstance;
import io.rapidw.easybpmn.task.TaskQuery;
import jakarta.persistence.EntityManager;
import lombok.val;

import java.util.List;

public class TaskInstanceRepository extends AbstractRepository<TaskInstance> {
    public TaskInstanceRepository(ThreadLocal<EntityManager> entityManagerThreadLocal) {
        super(entityManagerThreadLocal);
    }

    public List<TaskInstance> query(TaskQuery taskQuery) {
        val entityManager = entityManagerThreadLocal.get();
        val transaction = entityManager.getTransaction();
        transaction.begin();
        JPAQuery<TaskInstance> executionJPAQuery = new JPAQuery<>(entityManagerThreadLocal.get());
        val q = QTaskInstance.taskInstance;
        val res = executionJPAQuery.from(q).where(q.processInstance.id.eq(taskQuery.getId())).fetch();
        transaction.commit();
        return res;
    }
}
