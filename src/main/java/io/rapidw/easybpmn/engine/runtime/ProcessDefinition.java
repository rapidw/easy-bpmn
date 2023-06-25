package io.rapidw.easybpmn.engine.runtime;

import io.rapidw.easybpmn.engine.ProcessEngine;
import io.rapidw.easybpmn.engine.model.Process;
import io.rapidw.easybpmn.engine.serialization.Bpmn;
import io.rapidw.easybpmn.registry.Deployment;
import io.rapidw.easybpmn.utils.ModelUtils;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Data
@Slf4j
public class ProcessDefinition {
    private ProcessEngine processEngine;

    private Integer id;
    private Deployment deployment;
    private Process process;

    @Builder
    public ProcessDefinition(ProcessEngine processEngine, Deployment deployment) {
        this.processEngine = processEngine;
        this.deployment = deployment;
        this.id = deployment.getId();
        buildModel();
    }

    @SneakyThrows
    public void buildModel() {
        val bpmn = this.processEngine.getObjectMapper().readValue(this.deployment.getModel(), Bpmn.class);
        val process = bpmn.getDefinitions().getProcesses().get(0);
        this.process = ModelUtils.buildModel(process);
    }
}
