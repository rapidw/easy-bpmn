package io.rapidw.easybpmn.engine.repository;

import io.rapidw.easybpmn.engine.runtime.Variable;
import jakarta.persistence.EntityManager;

public class VariableRepository extends AbstractRepository<Variable> {
    public VariableRepository(ThreadLocal<EntityManager> threadLocal) {
        super(threadLocal);
    }
}
