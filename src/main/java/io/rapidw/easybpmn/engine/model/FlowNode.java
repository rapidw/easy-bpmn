package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.behavior.Behavior;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class FlowNode extends FlowElement {
    private List<SequenceFlow> incoming = new ArrayList<>();
    private List<SequenceFlow> outgoing = new ArrayList<>();
    private Behavior behavior;
}
