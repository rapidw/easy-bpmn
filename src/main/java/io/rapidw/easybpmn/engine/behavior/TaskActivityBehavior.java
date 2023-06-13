package io.rapidw.easybpmn.engine.behavior;

import io.rapidw.easybpmn.engine.Execution;

public class TaskActivityBehavior implements Behavior{
    public static TaskActivityBehavior INSTANCE = new TaskActivityBehavior();

    @Override
    public void execute(Execution execution) {
        // do nothing
    }
}
