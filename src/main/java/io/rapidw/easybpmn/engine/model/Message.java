package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.ProcessEngine;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Message extends RootElement {
    private String name;
    private ItemDefinition itemRef;
    private List<Error> errorRef;
    private BaseElement implementationRef;
}
