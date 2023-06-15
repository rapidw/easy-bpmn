package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.Execution;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Task extends Activity {
    public static class TaskActivityBehavior implements Behavior {
        public static TaskActivityBehavior INSTANCE = new TaskActivityBehavior();

        @Override
        public void execute(Execution execution) {
            // do nothing
        }
    }
}
