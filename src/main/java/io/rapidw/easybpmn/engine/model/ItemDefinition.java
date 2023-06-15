package io.rapidw.easybpmn.engine.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
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
