package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.Execution;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task extends Activity {

    public static class TaskActivityBehavior extends Behavior {
        public static final TaskActivityBehavior INSTANCE = new TaskActivityBehavior();

        @Override
        public void execute(Execution execution) {
            // do nothing
        }
    }
}
