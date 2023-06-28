package io.rapidw.easybpmn

import com.fasterxml.jackson.databind.ObjectMapper
import io.rapidw.easybpmn.engine.ProcessEngine
import io.rapidw.easybpmn.engine.ProcessEngineConfig
import io.rapidw.easybpmn.engine.repository.TaskInstanceRepository
import io.rapidw.easybpmn.engine.serialization.*
import io.rapidw.easybpmn.registry.ProcessRegistry
import io.rapidw.easybpmn.registry.ProcessRegistryConfig
import io.rapidw.easybpmn.query.TaskInstanceQuery
import spock.lang.Specification

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ProcessEngineSpec extends Specification {
//    RepositoryService repositoryService = Spy()
//    RuntimeService runtimeService = Stub()
    TaskInstanceRepository taskService = Stub()
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

        process.getFlowElements().addAll(sf1, sf2)
        definition.getProcesses().add(process)
        bpmn.setDefinitions(definition)

        def str = objectMapper.writeValueAsString(bpmn)
        println str

//        def another_process = objectMapper.readValue(str, Bpmn.class)

        def registryConfig = ProcessRegistryConfig.builder().build()
        def registry = new ProcessRegistry(registryConfig)

        def engineConfig = ProcessEngineConfig.builder().build()
        def engine = new ProcessEngine(registry, engineConfig)

        def processDefinition = registry.deploy(str)

        def variable = new MyVariable()
        def processInstance = engine.startProcessInstanceById(processDefinition, variable)
        new CountDownLatch(1).await(1, TimeUnit.SECONDS)
        def tasks = engine.queryTask(TaskInstanceQuery.builder().id(1).build())
        new CountDownLatch(1).await(1, TimeUnit.SECONDS)
        tasks[0].complete(new MyVariable().setX(1))
        new CountDownLatch(1).await(Integer.MAX_VALUE, TimeUnit.SECONDS)
        expect:
        1 == 1

    }

}
