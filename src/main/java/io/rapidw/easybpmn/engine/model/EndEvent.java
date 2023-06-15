package io.rapidw.easybpmn.engine.model;

public class EndEvent extends Event {

    public static class NoneEndEventBehavior implements Behavior {
        public static NoneEndEventBehavior INSTANCE = new NoneEndEventBehavior();
    }
}
