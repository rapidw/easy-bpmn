package io.rapidw.easybpmn.registry;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import io.rapidw.easybpmn.engine.runtime.DeploymentQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.List;

@Slf4j
public class DeploymentService {
    private final EntityManagerFactory entityManagerFactory;
    public DeploymentService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public Integer deploy(EntityManager entityManager, Deployment deployment) {
        log.debug("deploy start");
        val id = persistAndGetId(deployment);
        log.debug("deploy finished");
        return id;
    }

    public List<Deployment> queryProcessDefinition(DeploymentQuery query) {
        val q = QDeployment.deployment;
        JPAQuery<Deployment> jpaQuery = new JPAQuery<>(entityManagerFactory.createEntityManager());
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

    private Integer persistAndGetId(Deployment deployment) {
        val entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(deployment);
        entityManager.flush();
        entityManager.getTransaction().commit();
        return deployment.getId();
    }
}
