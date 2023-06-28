package io.rapidw.easybpmn.engine.model;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EndEvent extends Event {

    public static class NoneEndEventBehavior extends FlowNodeBehavior {
        public final static EndEvent.NoneEndEventBehavior INSTANCE = new EndEvent.NoneEndEventBehavior();

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
