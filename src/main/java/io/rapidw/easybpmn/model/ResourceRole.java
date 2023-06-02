package io.rapidw.easybpmn.model;

import java.util.List;

public class ResourceRole extends BaseElement {
    private Resource resourceRef;
    private ResourceAssignmentExpression resourceAssignmentExpression;
    private List<ResourceParameterBinding> resourceParameterBindings;
}
