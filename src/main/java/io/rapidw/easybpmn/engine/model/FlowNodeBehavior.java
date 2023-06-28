package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.ProcessEngine;
import io.rapidw.easybpmn.engine.operation.EnterFlowElementOperation;
import io.rapidw.easybpmn.engine.operation.LeaveFlowElementOperation;
import io.rapidw.easybpmn.engine.runtime.Execution;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class FlowNodeBehavior {
    protected ProcessEngine processEngine;
    protected Execution execution;

    public void onEnter(Execution execution) {
        prepare(execution);
        onEnter();
    }

    protected void onEnter() {
        notImplemented();
    }

    public void onLeave(Execution execution) {
        prepare(execution);
        onLeave();
    }

    protected void onLeave() {
        notImplemented();
    }

    private void prepare(Execution execution) {
        this.processEngine = execution.getProcessEngine();
        this.execution = execution;
    }

    protected void planLeave() {
        planLeave(this.execution);
    }

    protected void planLeave(Execution execution) {
        execution.getProcessEngine().addOperation(
            LeaveFlowElementOperation.builder()
                .executionId(execution.getId())
                .build()
        );
    }

    protected void planLeave(Execution currentExecution, List<SequenceFlow> sequenceFlows) {
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
        executions.forEach(this::planLeave);
    }

    protected void planEnter(Execution execution) {
        execution.getProcessEngine().addOperation(EnterFlowElementOperation.builder()
            .executionId(execution.getId())
            .build());
    }

    protected void doLeave() {

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
            outgoingExecutions.forEach(this::planEnter);
        } else {
            log.debug("execution finished");
        }
    }

    private void notImplemented() {
        log.debug("not implement behavior");
    }

    protected List<SequenceFlow> getOutgoingOfCurrentFlowNode(Execution execution) {
        return ((FlowNode) execution.getProcessEngine().getProcessDefinitionManager().get(execution.getProcessInstance().getDeploymentId())
            .getProcess().getFlowElementMap().get(execution.getCurrentFlowElementId())).getOutgoing();
    }
}
