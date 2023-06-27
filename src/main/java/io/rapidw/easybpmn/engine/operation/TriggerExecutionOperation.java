package io.rapidw.easybpmn.engine.operation;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class TriggerExecutionOperation extends AbstractOperation {
    //TODO  what's difference between this and LeaveFlowElementOperation?--event?
    @Override
    public void execute() {

    }
}
