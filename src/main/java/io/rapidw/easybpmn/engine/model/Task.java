package io.rapidw.easybpmn.engine.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task extends Activity {

    public static class TaskActivityBehavior extends FlowNodeBehavior {
        public static final TaskActivityBehavior INSTANCE = new TaskActivityBehavior();

        @Override
        public void onEnter() {
            // do nothing
        }
    }
}
