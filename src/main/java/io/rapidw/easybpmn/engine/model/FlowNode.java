package io.rapidw.easybpmn.engine.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FlowNode extends FlowElement {
    private List<SequenceFlow> incoming = new ArrayList<>();
    private List<SequenceFlow> outgoing = new ArrayList<>();
    private Behavior behavior;
}
