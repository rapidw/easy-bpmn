package io.rapidw.easybpmn.engine.runtime;

public class ActivityInstance {

    public enum State {
        READY,
        ACTIVE,
        COMPLETING,
        COMPLETED,
        FAILING,
        WITHDRAWN,
        TERMINATING,
        COMPENSATING,
        COMPENSATED,
        FAILED,
        TERMINATED
    }
}
