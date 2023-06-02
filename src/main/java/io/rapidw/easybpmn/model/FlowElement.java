package io.rapidw.easybpmn.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FlowElement extends BaseElement {
    private String name;
    private String documentation;
}
