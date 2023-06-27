package io.rapidw.easybpmn.engine.serialization;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Activity extends FlowNode {
    private boolean isForCompensation;
    private LoopCharacteristics loopCharacteristics;
}
