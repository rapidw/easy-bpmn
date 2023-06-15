package io.rapidw.easybpmn.engine.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Operation extends BaseElement {
    private Message inMessageRef;
    private Message outMessageRef;

}
