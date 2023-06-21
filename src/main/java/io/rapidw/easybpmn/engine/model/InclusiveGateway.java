package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.Execution;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InclusiveGateway extends Gateway {

    public static class InclusiveGatewayBehavior extends Behavior {
        public static final InclusiveGateway.InclusiveGatewayBehavior INSTANCE = new InclusiveGateway.InclusiveGatewayBehavior();

        @Override
        public void execute(Execution execution) {
            leave(execution);
        }
    }
}
