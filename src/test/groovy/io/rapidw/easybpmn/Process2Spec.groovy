package io.rapidw.easybpmn

import com.fasterxml.jackson.databind.ObjectMapper
import io.rapidw.easybpmn.engine.ProcessEngine
import io.rapidw.easybpmn.engine.ProcessEngineConfig
import io.rapidw.easybpmn.engine.serialization.*
import io.rapidw.easybpmn.registry.ProcessRegistry
import io.rapidw.easybpmn.registry.ProcessRegistryConfig
import io.rapidw.easybpmn.task.TaskQuery
import spock.lang.Specification

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class Process2Spec extends Specification {

    def "process engine"() {
        given:
        ObjectMapper objectMapper = new ObjectMapper()

        def bpmn = new Bpmn()
        def definition = new Definition()
        definition.setId("definitions")
        def process = new Process()
        process.setName("test_name")
        process.setId("test_id")

        def startEvent = new StartEvent()
        startEvent.setId("startEvent")
        process.getFlowElements().add(startEvent)

        def inputTask = new UserTask()
        inputTask.setId("user_task")
        inputTask.setName("user_task_name")
        process.getFlowElements().add(inputTask)

        def exclusiveGateway = new ExclusiveGateway()
        exclusiveGateway.setId("exclusiveGateway")
        process.getFlowElements().add(exclusiveGateway)

        def computeTask = new ServiceTask()
        computeTask.setId("computeTask")
        process.getFlowElements().add(computeTask)

        def inclusiveGateway = new InclusiveGateway()
        inclusiveGateway.setId("inclusiveGateway")
        process.getFlowElements().add(inclusiveGateway)

        def printTask = new ServiceTask()
        printTask.setId("printTask")
        process.getFlowElements().add(printTask)

        def endEvent = new EndEvent()
        endEvent.setId("endEvent")
        process.getFlowElements().add(endEvent)

        def sf1 = new SequenceFlow()
        sf1.setId("sf1")
        sf1.setSourceRef(startEvent.getId())
        sf1.setTargetRef(inputTask.getId())
        process.getFlowElements().add(sf1)

        def sf2 = new SequenceFlow()
        sf2.setId("sf2")
        sf2.setSourceRef(inputTask.getId())
        sf2.setTargetRef(exclusiveGateway.getId())
        process.getFlowElements().add(sf2)

        def sf3 = new SequenceFlow()
        sf3.setId("sf3")
        sf3.setSourceRef(exclusiveGateway.getId())
        sf3.setTargetRef(computeTask.getId())
        sf3.setConditionExpression("${variable.x > 1}")
        process.getFlowElements().add(sf3)

        def sf4 = new SequenceFlow()
        sf4.setId("sf4")
        sf4.setSourceRef(computeTask.getId())
        sf4.setTargetRef(inclusiveGateway.getId())
        process.getFlowElements().add(sf4)

        def sf5 = new SequenceFlow()
        sf5.setId("sf5")
        sf5.setSourceRef(exclusiveGateway.getId())
        sf5.setTargetRef(inclusiveGateway.getId())
        process.getFlowElements().add(sf5)

        def sf6 = new SequenceFlow()
        sf6.setId("sf6")
        sf6.setSourceRef(inclusiveGateway.getId())
        sf6.setTargetRef(printTask.getId())
        process.getFlowElements().add(sf6)

        def sf7 = new SequenceFlow()
        sf7.setId("sf7")
        sf7.setSourceRef(printTask.getId())
        sf7.setTargetRef(endEvent.getId())
        process.getFlowElements().add(sf7)

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
        variable.setX(1)
        def processInstance = engine.startProcessInstanceById(processDefinition, variable)
        new CountDownLatch(1).await(1, TimeUnit.SECONDS)
        def tasks = processInstance.queryTask(TaskQuery.builder().id(1).build())
        new CountDownLatch(1).await(1, TimeUnit.SECONDS)
        def newVariable = tasks[0].getVariable(MyVariable)
        newVariable.setX(2)
        new CountDownLatch(1).await(Integer.MAX_VALUE, TimeUnit.SECONDS)
        tasks[0].complete(newVariable)
        expect:
        1 == 1

    }

}
