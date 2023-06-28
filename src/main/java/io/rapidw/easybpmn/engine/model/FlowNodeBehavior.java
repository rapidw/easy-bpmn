package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.ProcessEngine;
import io.rapidw.easybpmn.engine.operation.AbstractOperation;
import io.rapidw.easybpmn.engine.operation.EnterFlowElementOperation;
import io.rapidw.easybpmn.engine.operation.LeaveFlowElementOperation;
import io.rapidw.easybpmn.engine.runtime.Execution;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class FlowNodeBehavior {
    protected ProcessEngine processEngine;
    protected Execution execution;

    public List<AbstractOperation> onEnter(Execution execution) {
        prepare(execution);
        return onEnter();
    }

    // default
    protected List<AbstractOperation> onEnter() {
        notImplemented();
        return null;
    }

    public List<AbstractOperation> onLeave(Execution execution) {
        prepare(execution);
        return onLeave();
    }

    // default
    protected List<AbstractOperation> onLeave() {
        notImplemented();
        return null;
    }

    private void prepare(Execution execution) {
        this.processEngine = execution.getProcessEngine();
        this.execution = execution;
    }

    protected List<AbstractOperation> planLeave() {
        return planLeave(this.execution);
    }

    protected List<AbstractOperation> planLeave(Execution execution) {
        return List.of(LeaveFlowElementOperation.builder()
                .executionId(execution.getId())
                .build());
    }

    protected List<AbstractOperation> planEnter(Execution currentExecution, List<SequenceFlow> sequenceFlows) {
        List<Execution> executions = new ArrayList<>(sequenceFlows.size());
        // reuse current execution
        currentExecution.setCurrentFlowElementId(sequenceFlows.get(0).getId());
        executions.add(currentExecution);

        for (int i = 1; i < sequenceFlows.size(); i++) {
            val sf = sequenceFlows.get(i);
            val child = Execution.builder()
                .processInstance(currentExecution.getProcessInstance())
                .parent(currentExecution)
                .initialFlowElement(sf)
                .active(true)
                .variable(currentExecution.getVariable())
                .build();
            executions.add(child);
        }
        return executions.stream().map(this::planEnter).flatMap(List::stream).collect(Collectors.toList());
    }

    protected List<AbstractOperation> planEnter(Execution execution) {
        return List.of(EnterFlowElementOperation.builder()
            .executionId(execution.getId())
            .build());
    }

    protected List<AbstractOperation> doLeave() {

        val outgoingSequenceFlows = getOutgoingOfCurrentFlowNode(this.execution);
        if (!outgoingSequenceFlows.isEmpty()) {
            val outgoingExecutions = new ArrayList<Execution>(outgoingSequenceFlows.size());

            //reuse current execution
            this.execution.setCurrentFlowElementId(outgoingSequenceFlows.get(0).getId());
            outgoingExecutions.add(this.execution);

            if (outgoingSequenceFlows.size() > 1) {
                for (int i = 1; i < outgoingSequenceFlows.size(); i++) {
                    val sf = outgoingSequenceFlows.get(i);
                    val parent = this.execution.getParent() != null ? this.execution.getParent() : this.execution;

                    val child = Execution.builder()
                        .processInstance(this.execution.getProcessInstance())
                        .parent(parent)
                        .initialFlowElement(sf)
                        .active(true)
                        .variable(this.execution.getVariable())
                        .build();
                    this.execution.getChildren().add(child);
                    outgoingExecutions.add(child);
                }
            }
            return outgoingExecutions.stream().map(execution -> EnterFlowElementOperation.builder()
                .executionId(execution.getId())
                .build()).collect(Collectors.toList());
        } else {
            log.debug("execution finished");
            return Collections.emptyList();
        }
    }

    private void notImplemented() {
        throw new ProcessEngineException("not implement behavior");
    }

    protected List<SequenceFlow> getOutgoingOfCurrentFlowNode(Execution execution) {
        return ((FlowNode) execution.getProcessEngine().getProcessDefinitionManager().get(execution.getProcessInstance().getDeploymentId())
            .getProcess().getFlowElementMap().get(execution.getCurrentFlowElementId())).getOutgoing();
    }
}
