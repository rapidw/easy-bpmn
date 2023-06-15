package io.rapidw.easybpmn.engine.runtime;

public class ActivityInstance {

    public enum State {
        INACTIVE,
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
