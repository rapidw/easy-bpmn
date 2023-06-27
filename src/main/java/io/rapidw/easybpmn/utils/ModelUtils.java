package io.rapidw.easybpmn.utils;

import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.model.FlowElement;
import io.rapidw.easybpmn.engine.serialization.Process;
import io.rapidw.easybpmn.engine.serialization.*;
import lombok.val;

import java.util.LinkedList;
import java.util.List;

public class ModelUtils {
    public static io.rapidw.easybpmn.engine.model.Process buildModel(Process process) {
        val processModel = new io.rapidw.easybpmn.engine.model.Process();
        val sfs = new LinkedList<SequenceFlow>();
        val needPostProcess = new LinkedList<FlowElementTuple>();
        process.getFlowElements().forEach(fe -> {
            FlowElement flowElement;
            if (fe instanceof StartEvent startEvent) {
                val startEventModel = new io.rapidw.easybpmn.engine.model.StartEvent();
                startEventModel.setId(startEvent.getId());
                startEventModel.setFlowElementBehavior(io.rapidw.easybpmn.engine.model.StartEvent.NoneStartEventBehavior.INSTANCE);
                processModel.setInitialFlowElement(startEventModel); // todo : set initial flow element
                flowElement = startEventModel;
            } else if (fe instanceof UserTask userTask) {
                val userTaskModel = new io.rapidw.easybpmn.engine.model.UserTask();
                userTaskModel.setId(userTask.getId());
                userTaskModel.setName(userTask.getName());
                userTaskModel.setAssignee(userTask.getAssignee());
                userTaskModel.setCandidates(userTask.getCandidates());
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
                inclusiveGatewayModel.setFlowElementBehavior(inclusiveGatewayModel.new InclusiveGatewayBehavior());
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
            val res = processModel.getFlowElementMap().put(flowElement.getId(), flowElement);
            if (res != null) {
                throw new ProcessEngineException("duplicate flow element id " + flowElement.getId());
            }
        });

        handleSequenceFlows(processModel, sfs);
        postProcess(processModel, needPostProcess);
        return processModel;
    }

    private static void handleSequenceFlows(io.rapidw.easybpmn.engine.model.Process processModel, List<SequenceFlow> sfs) {
        sfs.forEach(sf -> {
            val model = (io.rapidw.easybpmn.engine.model.SequenceFlow) processModel.getFlowElementMap().get(sf.getId());
            val source = processModel.getFlowElementMap().get(sf.getSourceRef());
            val target = processModel.getFlowElementMap().get(sf.getTargetRef());
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

    private static void postProcess(io.rapidw.easybpmn.engine.model.Process processModel, List<FlowElementTuple> needPostProcess) {
        needPostProcess.forEach(tuple -> {
            if (tuple.getModel() instanceof io.rapidw.easybpmn.engine.model.ExclusiveGateway exclusiveGatewayModel) {
                val exclusiveGateway = (ExclusiveGateway) tuple.getSerialization();
                if (exclusiveGateway.getDefaultFlow() != null) {
                    val defaultSequenceFlow = (io.rapidw.easybpmn.engine.model.SequenceFlow) processModel.getFlowElementMap().get(exclusiveGateway.getDefaultFlow());
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
