package io.rapidw.easybpmn.engine.model;

public class EndEvent extends Event {

    public static class NoneEndEventBehavior extends FlowElementBehavior {
        public final static EndEvent.NoneEndEventBehavior INSTANCE = new EndEvent.NoneEndEventBehavior();
    }
}
