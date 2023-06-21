package io.rapidw.easybpmn.engine.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Resource extends BaseElement {
    private String name;
    private List<ResourceParameter> resourceParameters;

}
