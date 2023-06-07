package io.rapidw.easybpmn.engine.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
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
