package io.rapidw.easybpmn.engine.runtime;

import io.rapidw.easybpmn.ProcessEngine;
import io.rapidw.easybpmn.engine.model.Process;
import io.rapidw.easybpmn.engine.model.StartEvent;
import io.rapidw.easybpmn.engine.serialization.Bpmn;
import io.rapidw.easybpmn.registry.Deployment;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Data
@Slf4j
public class ProcessDefinition {
    private ProcessEngine processEngine;

    private Deployment deployment;
    private Process process;

    @Builder
    public ProcessDefinition(ProcessEngine processEngine, Deployment deployment) {
        this.processEngine = processEngine;
        this.deployment = deployment;
        buildModel();
    }

    @SneakyThrows
    public Process buildModel() {
        val bpmn = this.processEngine.getObjectMapper().readValue(this.deployment.getModel(), Bpmn.class);
        val process = bpmn.getDefinitions().getProcesses().get(0);

        val processModel = new Process();
        val startEvent = process.getStartEvent();
        if (startEvent != null) {
            val startEventModel = new StartEvent();
            startEventModel.setOutgoing();
            processModel.setInitialFlowElement(startEvent);
        }

    }
}
