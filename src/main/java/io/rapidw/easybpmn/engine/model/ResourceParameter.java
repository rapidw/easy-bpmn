package io.rapidw.easybpmn.engine.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceParameter extends BaseElement {
    private String name;
    private Boolean isRequired;
}
