package io.rapidw.easybpmn.engine.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartEvent extends Event {

    public static class NoneStartEventBehavior extends FlowNodeBehavior {
        public static final NoneStartEventBehavior INSTANCE = new NoneStartEventBehavior();

        @Override
        protected void onEnter() {
            planLeave();
        }

        @Override
        protected void onLeave() {
            doLeave();
        }

    }
}
