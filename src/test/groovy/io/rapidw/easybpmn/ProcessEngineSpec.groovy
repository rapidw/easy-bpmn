package io.rapidw.easybpmn

import com.fasterxml.jackson.databind.ObjectMapper
import io.rapidw.easybpmn.engine.repository.TaskRepository
import io.rapidw.easybpmn.engine.serialization.*
import io.rapidw.easybpmn.task.TaskQuery
import spock.lang.Specification

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ProcessEngineSpec extends Specification {
//    RepositoryService repositoryService = Spy()
//    RuntimeService runtimeService = Stub()
    TaskRepository taskService = Stub()
//    HistoryService historyService = Stub()

    def "process engine"() {
        given:
        ObjectMapper objectMapper = new ObjectMapper()

        def bpmn = new Bpmn()
        def definition = new Definition()
        definition.setId("definitions")
        def process = new Process()
        process.setName("test_name")
        process.setId("test_id")

        def startEvent = new StartEvent();
        startEvent.setId("startEvent")
        process.getFlowElements().add(startEvent)

        def endEvent = new EndEvent()
        endEvent.setId("endEvent")
        process.getFlowElements().add(endEvent)

        def userTask = new UserTask()
        userTask.setId("user_task")
        userTask.setName("user_task_name")
        process.getFlowElements().add(userTask)

        def sf1 = new SequenceFlow()
        sf1.setId("sf1")
        sf1.setSourceRef(startEvent.getId())
        sf1.setTargetRef(userTask.getId())

        def sf2 = new SequenceFlow()
        sf2.setId("sf2")
        sf2.setSourceRef(userTask.getId())
        sf2.setTargetRef(endEvent.getId())

        process.getSequenceFlows().addAll(sf1, sf2)
        definition.getProcesses().add(process)
        bpmn.setDefinitions(definition)

        def str = objectMapper.writeValueAsString(bpmn)
        println str

//        def another_process = objectMapper.readValue(str, Bpmn.class)

        def registryConfig = new ProcessRegistryConfig()
        def registry = new ProcessRegistry(registryConfig)

        def engineConfig = ProcessEngineConfig.builder().build()
        def engine = new ProcessEngine(registry, engineConfig)

        def processDefinition = registry.deploy(str)

        def variable = new MyVariable()
        def processInstance = engine.startProcessInstanceById(processDefinition, variable)
        new CountDownLatch(1).await(1, TimeUnit.SECONDS)
        def tasks = processInstance.queryTask(TaskQuery.builder().id(1).build())
        new CountDownLatch(1).await(1, TimeUnit.SECONDS)
        tasks[0].complete(new Apply().setReason("apply"))
        new CountDownLatch(1).await(Integer.MAX_VALUE, TimeUnit.SECONDS)
        expect:
        1 == 1

    }

}
