package io.rapidw.easybpmn.engine.model;

import java.util.List;

public class ResourceRole extends BaseElement {
    private Resource resourceRef;
    private ResourceAssignmentExpression resourceAssignmentExpression;
    private List<ResourceParameterBinding> resourceParameterBindings;
}
