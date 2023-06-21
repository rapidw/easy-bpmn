package io.rapidw.easybpmn.engine.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SequenceFlow extends FlowElement {

    private FlowNode sourceRef;
    private FlowNode targetRef;
    private String conditionExpression;
    private Boolean isImmediate;
}
