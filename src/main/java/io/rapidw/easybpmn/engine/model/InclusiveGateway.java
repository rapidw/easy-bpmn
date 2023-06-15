package io.rapidw.easybpmn.engine.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InclusiveGateway extends Gateway {

    public static class InclusiveGatewayBehavior implements Behavior {
        public static InclusiveGatewayBehavior INSTANCE = new InclusiveGatewayBehavior();

    }
}
