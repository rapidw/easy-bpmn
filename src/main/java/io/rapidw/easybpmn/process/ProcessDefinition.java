package io.rapidw.easybpmn.process;

import io.rapidw.easybpmn.process.ProcessInstance;

public class ProcessDefinition {
    private Integer id;

    public ProcessInstance startProcessInstance() {
        return new ProcessInstance();
    }
}
