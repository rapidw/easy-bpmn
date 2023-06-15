package io.rapidw.easybpmn.engine.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Message extends RootElement {
    private String name;
    private ItemDefinition itemRef;
    private List<Error> errorRef;
    private BaseElement implementationRef;
}
