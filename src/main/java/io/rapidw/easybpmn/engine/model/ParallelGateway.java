package io.rapidw.easybpmn.engine.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParallelGateway extends Gateway {

    public static class ParallelGatewayBehavior extends Behavior {
        public final static ParallelGateway.ParallelGatewayBehavior INSTANCE = new ParallelGateway.ParallelGatewayBehavior();
    }
}
