package io.rapidw.easybpmn.engine.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StandardLoopCharacteristics extends LoopCharacteristics {
    private Boolean testBefore = false;
    private Integer loopMaximum;
    private Expression loopCondition;
}
