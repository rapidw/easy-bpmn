package io.rapidw.easybpmn.engine.serialization;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SequenceFlow extends FlowElement {
    private String sourceRef;
    private String targetRef;
}
