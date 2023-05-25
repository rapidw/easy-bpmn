package io.rapidw.easybpmn.process.model;

import java.util.List;

public class ResourceRole extends BaseElement {
    private Resource resourceRef;
    private ResourceAssignmentExpression resourceAssignmentExpression;
    private List<ResourceParameterBinding> resourceParameterBindings;
}
