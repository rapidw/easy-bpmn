package io.rapidw.easybpmn.engine.model;

import io.rapidw.easybpmn.engine.Execution;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceTask extends Task {

    private String implementation;
    private Operation operationRef;

    public static class ServiceTaskBehavior implements Behavior {
        public static ServiceTaskBehavior INSTANCE = new ServiceTaskBehavior();

        @Override
        public void execute(Execution execution) {
            // do nothing
        }
    }
}
