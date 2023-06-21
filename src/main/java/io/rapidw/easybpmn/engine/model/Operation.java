package io.rapidw.easybpmn.engine.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Operation extends BaseElement {
    private Message inMessageRef;
    private Message outMessageRef;
}
