package io.rapidw.easybpmn.registry;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import io.rapidw.easybpmn.engine.repository.AbstractRepository;
import io.rapidw.easybpmn.engine.runtime.DeploymentQuery;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.List;

@Slf4j
public class DeploymentService extends AbstractRepository<Deployment> {

    public Integer deploy(EntityManager entityManager, Deployment deployment) {
        log.debug("deploy start");
        val id = persistAndGetId(entityManager, deployment);
        log.debug("deploy finished");
        return id;
    }

    public List<Deployment> queryProcessDefinition(EntityManager entityManager, DeploymentQuery query) {
        val q = QDeployment.deployment;
        JPAQuery<Deployment> jpaQuery = new JPAQuery<>(entityManager);
        jpaQuery = jpaQuery.from(q);
        BooleanBuilder where = new BooleanBuilder();
        if (query.getId() != null) {
            where.and(q.id.eq(query.getId()));
        }
        if (query.getName() != null){
            where.and(q.name.eq(query.getName()));
        }
        return jpaQuery.where(where).fetch();
    }
}
