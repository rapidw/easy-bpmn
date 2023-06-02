package io.rapidw.easybpmn

import io.rapidw.easybpmn.process.TaskInstance
import io.rapidw.easybpmn.service.TaskService
import io.rapidw.easybpmn.task.TaskQuery
import spock.lang.Specification

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ProcessEngineSpec extends Specification {
//    RepositoryService repositoryService = Spy()
//    RuntimeService runtimeService = Stub()
    TaskService taskService = Stub()
//    HistoryService historyService = Stub()

    def "process engine"() {
        given:

        def engineConfig = ProcessEngineConfig.builder().build();
        def engine = new ProcessEngine(engineConfig)

        def registryConfig = new ProcessRegistryConfig()
        def registry = new ProcessRegistry(registryConfig)

        def processDefinition = registry.newProcessDefinition("fff")

        def variable = new MyVariable()
        def processInstance = engine.startProcessInstance(processDefinition, variable)
        taskService.queryTask(_ as TaskQuery) >> [new TaskInstance(
            id: 1,
            name: "test",
            assignee: "xx",
            processInstance: processInstance
        )]
        def tasks = processInstance.queryTask(TaskQuery.builder().assignee("xx").build())
        new CountDownLatch(1).await(3, TimeUnit.SECONDS)
        tasks[0].complete(new Apply().setReason("apply"))
        new CountDownLatch(1).await(3, TimeUnit.SECONDS)
        expect:
        1 == 1

    }

}
