package io.rapidw.easybpmn.engine.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResourceRole extends BaseElement {
    private Resource resourceRef;
    private ResourceAssignmentExpression resourceAssignmentExpression;
    private List<ResourceParameterBinding> resourceParameterBindings;
}
