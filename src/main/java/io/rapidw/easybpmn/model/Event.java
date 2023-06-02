package io.rapidw.easybpmn.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Event extends FlowNode {
    public Event(Type type) {
        this.type = type;
    }
    private Type type;

    public enum Type {
        START,
        END,
        CUSTOM,
        ;
    }
}
