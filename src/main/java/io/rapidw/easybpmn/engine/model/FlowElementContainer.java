package io.rapidw.easybpmn.engine.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FlowElementContainer extends FlowElement {
    private List<FlowElement> flowElements;
    private LaneSet laneSet;
}
