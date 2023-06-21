package io.rapidw.easybpmn.engine.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Gateway extends FlowNode {

    public enum GatewayDirection {
        UNSPECIFIED,
        MIXED,
        CONVERGING,
        DIVERGING,
        ;
    }

    private GatewayDirection direction;
}
