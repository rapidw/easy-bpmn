package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.operation.AbstractOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class EndEvent extends Event {

    public static class NoneEndEventBehavior extends FlowNodeBehavior {
        public final static EndEvent.NoneEndEventBehavior INSTANCE = new EndEvent.NoneEndEventBehavior();

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
