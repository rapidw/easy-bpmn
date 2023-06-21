package io.rapidw.easybpmn.engine.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceParameterBinding extends BaseElement {
    private ResourceParameter parameterRef;
    private Expression expression;
}
