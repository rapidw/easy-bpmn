package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.runtime.Execution;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task extends Activity {

    public static class TaskActivityBehavior extends FlowElementBehavior {
        public static final TaskActivityBehavior INSTANCE = new TaskActivityBehavior();

        @Override
        public void onEnter(Execution execution) {
            // do nothing
        }
    }
}
