package io.rapidw.easybpmn.engine;

import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.behavior.NoneEndEventBehavior;
import io.rapidw.easybpmn.engine.behavior.NoneStartEventBehavior;
import io.rapidw.easybpmn.engine.behavior.TaskActivityBehavior;
import io.rapidw.easybpmn.engine.model.FlowElement;
import io.rapidw.easybpmn.engine.model.FlowNode;
import io.rapidw.easybpmn.engine.model.Process;
import io.rapidw.easybpmn.engine.model.SequenceFlow;
import io.rapidw.easybpmn.engine.serialization.Bpmn;
import io.rapidw.easybpmn.engine.serialization.EndEvent;
import io.rapidw.easybpmn.engine.serialization.StartEvent;
import io.rapidw.easybpmn.engine.serialization.UserTask;
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
    public void  buildModel() {
        val bpmn = this.processEngine.getObjectMapper().readValue(this.deployment.getModel(), Bpmn.class);
        val process = bpmn.getDefinitions().getProcesses().get(0);

        val processModel = new Process();
        process.getFlowElements().forEach(fe -> {
            FlowElement model = null;
            if (fe instanceof StartEvent startEvent) {
                val startEventModel = new io.rapidw.easybpmn.engine.model.StartEvent();
                startEventModel.setId(startEvent.getId());
                startEventModel.setBehavior(NoneStartEventBehavior.INSTANCE);
                processModel.setInitialFlowElement(startEventModel);
                model = startEventModel;
            } else if (fe instanceof UserTask userTask) {
                val userTaskModel = new io.rapidw.easybpmn.engine.model.UserTask();
                userTaskModel.setId(userTask.getId());
                userTaskModel.setName(userTask.getName());
                userTaskModel.setBehavior(TaskActivityBehavior.INSTANCE);
                model = userTaskModel;
            } else if (fe instanceof EndEvent endEvent) {
                 val endEventModel = new io.rapidw.easybpmn.engine.model.EndEvent();
                 endEventModel.setId(endEvent.getId());
                 endEventModel.setBehavior(NoneEndEventBehavior.INSTANCE);
                 model= endEventModel;
            }
            if (model == null) {
                throw new ProcessEngineException("null model");
            }
            processModel.getFlowElementMap().put(model.getId(), model);
        });

        process.getSequenceFlows().forEach(sf -> {
            val model = new SequenceFlow();
            model.setId(sf.getId());
            val source = processModel.getFlowElementMap().get(sf.getSourceRef());
            val target = processModel.getFlowElementMap().get(sf.getTargetRef());
            if (source == null || target == null) {
                throw new ProcessEngineException("source or target is null");
            }
            if (source instanceof FlowNode sourceFn && target instanceof FlowNode targetFn) {
                model.setSourceRef(sourceFn);
                sourceFn.getOutgoing().add(model);
                model.setTargetRef(targetFn);
                targetFn.getIncoming().add(model);
            } else {
                throw new ProcessEngineException("source or target not instance of FlowNode");
            }
            processModel.getFlowElementMap().put(model.getId(), model);
        });
        this.process = processModel;
    }


}
