package io.rapidw.easybpmn.engine.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartEvent extends Event {

    public static class NoneStartEventBehavior extends Behavior {
        public static final NoneStartEventBehavior INSTANCE = new NoneStartEventBehavior();
    }
}
