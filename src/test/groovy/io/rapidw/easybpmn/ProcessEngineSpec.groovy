package io.rapidw.easybpmn

import io.rapidw.easybpmn.process.ProcessDefinition
import io.rapidw.easybpmn.process.model.EndEvent
import io.rapidw.easybpmn.process.model.Process
import io.rapidw.easybpmn.process.model.SequenceFlow
import io.rapidw.easybpmn.process.model.StartEvent
import io.rapidw.easybpmn.service.HistoryService
import io.rapidw.easybpmn.service.RepositoryService
import io.rapidw.easybpmn.service.RuntimeService
import io.rapidw.easybpmn.service.TaskService
import io.rapidw.easybpmn.process.TaskInstance
import io.rapidw.easybpmn.task.TaskQuery
import io.rapidw.easybpmn.process.model.UserTask
import spock.lang.Specification

class ProcessEngineSpec extends Specification {
    RepositoryService repositoryService = Stub()
    RuntimeService runtimeService = Stub()
    TaskService taskService = Stub()
    HistoryService historyService = Stub()

    def "process engine"() {
        given:

        def start = new StartEvent()
        def userTask = new UserTask()
        def end = new EndEvent()
        def sf1 = new SequenceFlow()
        sf1.setSourceRef(start)
        sf1.setTargetRef(userTask)
        start.getOutgoing().add(sf1)
        def sf2 = new SequenceFlow()
        sf2.setSourceRef(userTask)
        sf2.setTargetRef(end)
        userTask.getIncoming().add(sf1)
        userTask.getOutgoing().add(sf2)
        end.getIncoming().add(sf2)

        def process = Process.builder().initialFlowElement(start).build()
        def engine = new ProcessEngine(repositoryService, runtimeService, taskService, historyService)

        def processDefinition = ProcessDefinition.builder()
                .processEngine(engine)
                .process(process)
                .build()
//        def instance = new ProcessInstance(engine, processDefinition)
//        runtimeService >> instance
//        processDefinition.startProcessInstance() >> instance
        def variable = new ProcessVariable()
        def processInstance = processDefinition.startProcessInstance(variable)
        taskService.queryTask(_ as TaskQuery) >> [new TaskInstance(
                        id: 1,
                        name: "test",
                        assignee: "xx",
                        processInstance: processInstance
                )]
        def tasks = processInstance.queryTask(TaskQuery.builder().assignee("xx").build())
        expect:
        tasks[0].complete(new Apply().setReason("apply"))

    }

}