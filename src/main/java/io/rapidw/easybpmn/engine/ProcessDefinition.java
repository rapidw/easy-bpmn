package io.rapidw.easybpmn.engine;

import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.model.FlowElement;
import io.rapidw.easybpmn.engine.model.Process;
import io.rapidw.easybpmn.engine.serialization.*;
import io.rapidw.easybpmn.registry.Deployment;
import io.rapidw.easybpmn.utils.FlowElementTuple;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.LinkedList;
import java.util.List;

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

        this.process = new Process();
        val sfs = new LinkedList<SequenceFlow>();
        val needPostProcess = new LinkedList<FlowElementTuple>();
        process.getFlowElements().forEach(fe -> {
            FlowElement flowElement;
            if (fe instanceof StartEvent startEvent) {
                val startEventModel = new io.rapidw.easybpmn.engine.model.StartEvent();
                startEventModel.setId(startEvent.getId());
                startEventModel.setFlowElementBehavior(io.rapidw.easybpmn.engine.model.StartEvent.NoneStartEventBehavior.INSTANCE);
                this.process.setInitialFlowElement(startEventModel); // todo : set initial flow element
                flowElement = startEventModel;
            } else if (fe instanceof UserTask userTask) {
                val userTaskModel = new io.rapidw.easybpmn.engine.model.UserTask();
                userTaskModel.setId(userTask.getId());
                userTaskModel.setName(userTask.getName());
                userTaskModel.setFlowElementBehavior(userTaskModel.new UserTaskBehavior());
                flowElement = userTaskModel;
            } else if (fe instanceof EndEvent endEvent) {
                val endEventModel = new io.rapidw.easybpmn.engine.model.EndEvent();
                endEventModel.setId(endEvent.getId());
                endEventModel.setName(endEvent.getName());
                endEventModel.setFlowElementBehavior(io.rapidw.easybpmn.engine.model.EndEvent.NoneEndEventBehavior.INSTANCE);
                flowElement = endEventModel;
            } else if (fe instanceof ExclusiveGateway exclusiveGateway) {
                val exclusiveGatewayModel = new io.rapidw.easybpmn.engine.model.ExclusiveGateway();
                exclusiveGatewayModel.setId(exclusiveGateway.getId());
                exclusiveGatewayModel.setName(exclusiveGateway.getName());
                exclusiveGatewayModel.setFlowElementBehavior(exclusiveGatewayModel.new ExclusiveGatewayBehavior());
                flowElement = exclusiveGatewayModel;
                needPostProcess.add(new FlowElementTuple(exclusiveGatewayModel, exclusiveGateway));
            } else if (fe instanceof ParallelGateway parallelGateway) {
                val parallelGatewayModel = new io.rapidw.easybpmn.engine.model.ParallelGateway();
                parallelGatewayModel.setId(parallelGateway.getId());
                parallelGatewayModel.setName(parallelGateway.getName());
                parallelGatewayModel.setFlowElementBehavior(io.rapidw.easybpmn.engine.model.ParallelGateway.ParallelGatewayBehavior.INSTANCE);
                flowElement = parallelGatewayModel;
            } else if (fe instanceof InclusiveGateway inclusiveGateway) {
                val inclusiveGatewayModel = new io.rapidw.easybpmn.engine.model.InclusiveGateway();
                inclusiveGatewayModel.setId(inclusiveGateway.getId());
                inclusiveGatewayModel.setName(inclusiveGateway.getName());
                inclusiveGatewayModel.setFlowElementBehavior(io.rapidw.easybpmn.engine.model.InclusiveGateway.InclusiveGatewayBehavior.INSTANCE);
                flowElement = inclusiveGatewayModel;
            } else if (fe instanceof ServiceTask serviceTask) {
                val serviceTaskModel = new io.rapidw.easybpmn.engine.model.ServiceTask();
                serviceTaskModel.setId(serviceTask.getId());
                serviceTaskModel.setName(serviceTask.getName());
                serviceTaskModel.setExpression(serviceTask.getExpression());
                serviceTaskModel.setFlowElementBehavior(serviceTaskModel.new ServiceTaskBehavior());
                flowElement = serviceTaskModel;
            } else if (fe instanceof SequenceFlow sequenceFlow) {
                sfs.add(sequenceFlow);
                val sequenceFlowModel = new io.rapidw.easybpmn.engine.model.SequenceFlow();
                sequenceFlowModel.setId(sequenceFlow.getId());
                sequenceFlowModel.setConditionExpression(sequenceFlow.getConditionExpression());
                flowElement = sequenceFlowModel;
            } else {
                throw new ProcessEngineException("null model");
            }
            val res = this.process.getFlowElementMap().put(flowElement.getId(), flowElement);
            if (res != null) {
                throw new ProcessEngineException("duplicate flow element id " + flowElement.getId());
            }
        });

        handleSequenceFlows(sfs);
        postProcess(needPostProcess);
    }

    private void handleSequenceFlows(List<SequenceFlow> sfs) {
        sfs.forEach(sf -> {
            val model = (io.rapidw.easybpmn.engine.model.SequenceFlow) process.getFlowElementMap().get(sf.getId());
            val source = this.process.getFlowElementMap().get(sf.getSourceRef());
            val target = this.process.getFlowElementMap().get(sf.getTargetRef());
            if (source == null || target == null) {
                throw new ProcessEngineException("source or target is null");
            }
            if (source instanceof io.rapidw.easybpmn.engine.model.FlowNode sourceFn && target instanceof io.rapidw.easybpmn.engine.model.FlowNode targetFn) {
                model.setSourceRef(sourceFn);
                sourceFn.getOutgoing().add(model);
                model.setTargetRef(targetFn);
                targetFn.getIncoming().add(model);
            } else {
                throw new ProcessEngineException("source or target not instance of FlowNode");
            }
        });
    }

    private void postProcess(List<FlowElementTuple> needPostProcess) {
        needPostProcess.forEach(tuple -> {
            if (tuple.getModel() instanceof io.rapidw.easybpmn.engine.model.ExclusiveGateway exclusiveGatewayModel) {
                val exclusiveGateway = (ExclusiveGateway) tuple.getSerialization();
                if (exclusiveGateway.getDefaultFlow() != null) {
                    val defaultSequenceFlow = (io.rapidw.easybpmn.engine.model.SequenceFlow) this.process.getFlowElementMap().get(exclusiveGateway.getDefaultFlow());
                    if (defaultSequenceFlow == null) {
                        throw new ProcessEngineException("default sequence flow not found");
                    }
                    if (defaultSequenceFlow.getConditionExpression() != null) {
                        throw new ProcessEngineException("default sequence flow should not have condition expression");
                    }
                    exclusiveGatewayModel.setDefaultFlow(defaultSequenceFlow);
                }
            } else {
                throw new ProcessEngineException("not implemented");
            }
        });
    }
}
