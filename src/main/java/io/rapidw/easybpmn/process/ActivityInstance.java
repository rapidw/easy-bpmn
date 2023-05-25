package io.rapidw.easybpmn.process;

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
