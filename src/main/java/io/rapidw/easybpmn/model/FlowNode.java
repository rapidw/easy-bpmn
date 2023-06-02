package io.rapidw.easybpmn.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class FlowNode extends FlowElement {
    private List<SequenceFlow> incoming = new ArrayList<>();
    private List<SequenceFlow> outgoing = new ArrayList<>();

    public String toString() {
        return "FlowNode " + getName() + ", incoming=" + this.getIncoming().size() + ", outgoing=" + this.getOutgoing().size();
    }
}
