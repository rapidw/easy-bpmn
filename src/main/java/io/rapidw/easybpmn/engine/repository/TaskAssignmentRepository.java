package io.rapidw.easybpmn.engine.repository;

import io.rapidw.easybpmn.engine.runtime.TaskCandidate;
import jakarta.persistence.EntityManager;

public class TaskAssignmentRepository extends AbstractRepository<TaskCandidate> {

    public TaskAssignmentRepository(ThreadLocal<EntityManager> entityManagerThreadLocal) {
        super(entityManagerThreadLocal);
    }
}
