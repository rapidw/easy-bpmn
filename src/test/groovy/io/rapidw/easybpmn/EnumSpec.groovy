package io.rapidw.easybpmn

import com.querydsl.jpa.impl.JPAQuery
import io.rapidw.easybpmn.engine.ProcessEngine
import io.rapidw.easybpmn.engine.ProcessEngineConfig
import io.rapidw.easybpmn.engine.runtime.TaskCandidate
import io.rapidw.easybpmn.registry.ProcessRegistry
import io.rapidw.easybpmn.registry.ProcessRegistryConfig
import spock.lang.Specification

import java.util.concurrent.CountDownLatch

class EnumSpec extends Specification {

    def "test enum"() {
        given:
        def e = Enum.valueOf(TestEnum, "USER")
        def ta = new TaskCandidate();
        ta.setCandidateType(e)
        ta.setName("a")
        def registryConfig = ProcessRegistryConfig.builder().build()
        def registry = new ProcessRegistry(registryConfig)

        def engineConfig = ProcessEngineConfig.builder().build()
        def engine = new ProcessEngine(registry, engineConfig)
        def tran = engine.getEntityManagerThreadLocal().get().getTransaction();
        tran.begin()
        engine.getTaskCandidateRepository().persist(ta)
        tran.commit()

        def em = engine.getEntityManagerThreadLocal().get()
        tran = em.getTransaction()
        tran.begin()
        def query = new JPAQuery(em)
        def q = QTaskAssignment.taskAssignment
        def result = query.select(q).from(q).where(q.assigneeType.eq(e)).fetchOne()
        tran.commit()
        new CountDownLatch(1).await()

        expect:
        1 == 1

    }

    enum TestEnum {
        USER,
        GROUP
    }
}
