package io.rapidw.easybpmn.engine.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlowElement extends BaseElement {
    private String name;
    private String documentation;

}
