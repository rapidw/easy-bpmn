package io.rapidw.easybpmn.engine.serialization;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserTask extends FlowElement{
    private String name;
}
