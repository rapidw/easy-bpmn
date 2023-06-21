package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.ProcessEngine;

public class EndEvent extends Event {

    public static class NoneEndEventBehavior extends Behavior {
        public final static EndEvent.NoneEndEventBehavior INSTANCE = new EndEvent.NoneEndEventBehavior();
    }
}
