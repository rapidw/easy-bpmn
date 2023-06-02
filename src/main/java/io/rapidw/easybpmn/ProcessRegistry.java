package io.rapidw.easybpmn;

import io.rapidw.easybpmn.model.EndEvent;
import io.rapidw.easybpmn.model.Process;
import io.rapidw.easybpmn.model.SequenceFlow;
import io.rapidw.easybpmn.model.StartEvent;
import io.rapidw.easybpmn.model.UserTask;
import io.rapidw.easybpmn.process.ProcessDefinition;
import io.rapidw.easybpmn.process.ProcessDefinitionQuery;
import io.rapidw.easybpmn.service.DeploymentService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Getter;
import lombok.val;

import java.util.List;

public class ProcessRegistry {
    private ProcessRegistryConfig config;
    private EntityManagerFactory entityManagerFactory;
    @Getter
    private final DeploymentService deploymentService;

    public ProcessRegistry(ProcessRegistryConfig config) {
        this.config = config;
        this.entityManagerFactory = Persistence.createEntityManagerFactory("easy-bpmn");
        this.deploymentService = new DeploymentService(entityManagerFactory);
    }

    public ProcessDefinition newProcessDefinition(String processDefinition) {

        deploymentService.deploy(Deployment.builder().name("test").processDefinitionString(processDefinition).build());

        val start = new StartEvent();
        start.setName("start");

        val userTask = new UserTask();
        userTask.setName("userTask");

        val end = new EndEvent();
        end.setName("end");

        val sf1 = new SequenceFlow();
        sf1.setName("sf1");

        sf1.setSourceRef(start);
        sf1.setTargetRef(userTask);
        start.getOutgoing().add(sf1);

        val sf2 = new SequenceFlow();
        sf2.setName("sf2");

        sf2.setSourceRef(userTask);
        sf2.setTargetRef(end);

        userTask.getIncoming().add(sf1);
        userTask.getOutgoing().add(sf2);
        end.getIncoming().add(sf2);

        val process = Process.builder().initialFlowElement(start).build();

        return ProcessDefinition.builder().process(process).active(true).build();
    }

    public List<ProcessDefinition> queryProcessDefinition(ProcessDefinitionQuery query) {
        return deploymentService.queryProcessDefinition(query);
    }
}
