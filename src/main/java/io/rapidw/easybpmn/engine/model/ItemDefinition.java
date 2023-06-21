package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.ProcessEngine;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDefinition extends RootElement {
    private ItemKind itemKind;
    private BaseElement structureRef;
    private Import importRef;
    private boolean isCollection;

    public enum ItemKind {
        Physical,
        Information
    }
}
