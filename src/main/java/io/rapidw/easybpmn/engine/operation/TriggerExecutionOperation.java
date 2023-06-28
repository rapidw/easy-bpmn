package io.rapidw.easybpmn.engine.operation;

import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class TriggerExecutionOperation extends AbstractOperation {
    //TODO  what's difference between this and LeaveFlowElementOperation?--event?
    @Override
    public List<AbstractOperation> execute() {
        return null;
    }
}
