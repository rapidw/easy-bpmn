package io.rapidw.easybpmn.engine.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Error extends RootElement {
    private String name;
    private String errorCode;
    private ItemDefinition structureRef;
}
