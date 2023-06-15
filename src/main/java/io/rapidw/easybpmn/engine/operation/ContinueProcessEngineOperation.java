package io.rapidw.easybpmn.engine.operation;


import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.model.*;
import io.rapidw.easybpmn.engine.TaskInstance;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@SuperBuilder
@Slf4j
public class ContinueProcessEngineOperation extends AbstractEngineOperation {

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
            handleEvent(event);
        } else if (flowNode instanceof Activity activity) {
            handleActivity(activity);
        } else if (flowNode instanceof Gateway gateway) {
            handleGateway(gateway);
        } else {
            notImplemented();
        }
    }

    private void notImplemented() {
        throw new ProcessEngineException("invalid flow node " + currentFlowElement.getId() + " of type" + currentFlowElement.getClass().getSimpleName());
    }

    public void handleEvent(Event event) {
        if (event instanceof StartEvent startEvent) {
            startEvent.getBehavior().execute(this.execution);
        } else if (event instanceof EndEvent endEvent) {
            endEvent.getBehavior().execute(this.execution);
        } else {
            notImplemented();
        }
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

    private void handleGateway(Gateway gateway) {
        if (gateway instanceof ParallelGateway parallelGateway) {
            parallelGateway.getBehavior().execute(this.execution);
        } else if (gateway instanceof ExclusiveGateway exclusiveGateway) {
            exclusiveGateway.getBehavior().execute(this.execution);
        } else if (gateway instanceof InclusiveGateway inclusiveGateway) {
            inclusiveGateway.getBehavior().execute(this.execution);
        } else {
            notImplemented();
        }
    }
}
