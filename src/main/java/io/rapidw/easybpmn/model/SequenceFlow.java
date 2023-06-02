package io.rapidw.easybpmn.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SequenceFlow extends FlowElement {

    private FlowNode sourceRef;
    private FlowNode targetRef;
    private String conditionExpression;
    private Boolean isImmediate;

    public String toString() {
        return "SequenceFlow(sourceRef=" + this.getSourceRef() + ", targetRef=" + this.getTargetRef() + ", conditionExpression=" + this.getConditionExpression();
    }
}
