package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.operation.AbstractOperation;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StartEvent extends Event {

    public static class NoneStartEventBehavior extends FlowNodeBehavior {
        public static final NoneStartEventBehavior INSTANCE = new NoneStartEventBehavior();

        @Override
        protected List<AbstractOperation> onEnter() {
            return planLeave();
        }

        @Override
        protected List<AbstractOperation> onLeave() {
            return doLeave();
        }

    }
}
