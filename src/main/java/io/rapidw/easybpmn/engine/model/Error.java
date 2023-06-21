package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.ProcessEngine;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Error extends RootElement {
    private String name;
    private String errorCode;
    private ItemDefinition structureRef;
}
