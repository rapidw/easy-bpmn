package io.rapidw.easybpmn.engine.operation;

import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.List;

@SuperBuilder
public class EndExecutionOperation extends AbstractOperation {
    @Override
    public List<AbstractOperation> execute() {
        return Collections.emptyList();
    }
}
