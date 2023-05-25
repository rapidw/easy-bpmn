package io.rapidw.easybpmn.process.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Expression extends BaseElement {
    public enum Language {
        GROOVY,
    }

    private Language language;
    private String body;

    // todo: evaluatesToTypeRef
}
