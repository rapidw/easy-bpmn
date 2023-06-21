package io.rapidw.easybpmn.engine.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Expression extends BaseElement {
    public enum Language {
        GROOVY,
    }

    private Language language;
    private String body;

    // todo: evaluatesToTypeRef
}
