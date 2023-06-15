package io.rapidw.easybpmn.engine.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StartEvent extends Event {

    public class NoneStartEventBehavior implements Behavior {
//        public static NoneStartEventBehavior INSTANCE = new NoneStartEventBehavior();
    }
}
