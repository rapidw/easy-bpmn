package io.rapidw.easybpmn

import com.fasterxml.jackson.databind.ObjectMapper
import io.rapidw.easybpmn.engine.ProcessEngine
import io.rapidw.easybpmn.engine.ProcessEngineConfig
import io.rapidw.easybpmn.engine.runtime.ProcessInstance
import io.rapidw.easybpmn.engine.serialization.*
import io.rapidw.easybpmn.query.TaskInstanceQuery
import io.rapidw.easybpmn.registry.ProcessRegistry
import io.rapidw.easybpmn.registry.ProcessRegistryConfig
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class TaskInstanceQuerySpec extends Specification {

    @Shared
    ProcessRegistry registry
    @Shared
    ProcessEngine engine
    @Shared
    ProcessInstance processInstance

    def setupSpec() {

        def bpmn = new Bpmn()
        def definition = new Definition()
        definition.setId("definitions")
        def process = new Process()
        process.setName("test_name")
        process.setId("test_id")

        def startEvent = new StartEvent()
        startEvent.setId("startEvent")
        process.getFlowElements().add(startEvent)

        def endEvent = new EndEvent()
        endEvent.setId("endEvent")
        process.getFlowElements().add(endEvent)

        def userTask = new UserTask()
        userTask.setId("user_task")
        userTask.setName("user_task_name")
        userTask.getCandidates().add(new io.rapidw.easybpmn.engine.common.Candidate(
            name: "aaa",
            type: Candidate.USER
        ))
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


        def objectMapper = new ObjectMapper()
        def str = objectMapper.writeValueAsString(bpmn)
        println str

        def registryConfig = ProcessRegistryConfig.builder()
            .candidateEnumClass(Candidate).build()
        registry = new ProcessRegistry(registryConfig)

        def engineConfig = ProcessEngineConfig.builder()
            .build()
        engine = new ProcessEngine(registry, engineConfig)
        def processDefinition = registry.deploy(str)

        def variable = new MyVariable()
        this.processInstance = engine.startProcessInstanceById(processDefinition, variable)
        new CountDownLatch(1).await(1, TimeUnit.SECONDS)
    }

    enum Candidate {
        USER,
        GROUP
    }

    def "query by process instance id"() {
        when:
        def tasks = engine.queryTask(TaskInstanceQuery.builder().processInstanceId(this.processInstance.getId()).build())
        then:

        tasks.size() == 1
        with(tasks[0]) {
            it.processInstance.getId() == this.processInstance.getId()
            userTaskId == 'user_task'
            candidates.collect { it.candidate } as Set == [new io.rapidw.easybpmn.engine.common.Candidate(
                name: "aaa",
                type: Candidate.USER
            )] as Set
        }
    }

    def "query by candidate"() {
        when:
        def tasks = engine.queryTask(TaskInstanceQuery.builder().candidate(new io.rapidw.easybpmn.engine.common.Candidate(
            name: "aaa",
            type: Candidate.USER
        )).build())
        then:
        tasks.size() == 1
        with(tasks[0]) {
            it.processInstance.getId() == this.processInstance.getId()
            userTaskId == 'user_task'
            candidates.collect { it.candidate } as Set == [new io.rapidw.easybpmn.engine.common.Candidate(
                name: "aaa",
                type: Candidate.USER
            )] as Set
        }
    }
}
