package io.rapidw.easybpmn.engine.runtime.operation;


import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.model.*;
import io.rapidw.easybpmn.engine.runtime.TaskInstance;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@SuperBuilder
@Slf4j
public class ContinueProcessOperation extends AbstractOperation {

    @Override
    public void execute() {
        if (currentFlowElement instanceof FlowNode flowNode) {
            handleFlowNode(flowNode);
        } else if (currentFlowElement instanceof SequenceFlow sequenceFlow) {
            handleSequenceFlow(sequenceFlow);
        } else {
            throw new ProcessEngineException("invalid flow element " + currentFlowElement.getId() + " of type " + currentFlowElement.getClass().getSimpleName());
        }
    }

    private void handleFlowNode(FlowNode flowNode) {
        if (flowNode instanceof Event event) {
            if (event instanceof StartEvent startEvent) {
                planTakeOutgoingSequenceFlowsOperation(this.execution);
            } else {
                notImplemented();
            }
        } else if (flowNode instanceof Activity activity) {
            handleActivity(activity);
        } else {
            notImplemented();
        }
    }

    private void notImplemented() {
        throw new ProcessEngineException("invalid flow node " + currentFlowElement.getId() + " of type" + currentFlowElement.getClass().getSimpleName());
    }

    private void handleSequenceFlow(SequenceFlow sequenceFlow) {
        this.execution.setCurrentFlowElementId(sequenceFlow.getTargetRef().getId());
        this.processEngine.getExecutionRepository().merge(this.execution);
        planContinueProcessOperation(this.execution);
    }

    private void handleActivity(Activity activity) {
        if (activity instanceof UserTask userTask) {
            val taskInstance = TaskInstance.builder()
                .execution(execution)
                .userTask(userTask)
                .build();
            this.processEngine.getTaskRepository().persistAndGetId(taskInstance);
        }
    }
}
