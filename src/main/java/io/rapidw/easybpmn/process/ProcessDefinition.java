package io.rapidw.easybpmn.process;

import io.rapidw.easybpmn.ProcessEngine;
import io.rapidw.easybpmn.process.model.Process;
import lombok.Builder;
import lombok.Data;

@Data
public class ProcessDefinition {
    private ProcessEngine processEngine;

    private Integer id;
    private String name;
    private Boolean active;

    private Process process;

    @Builder
    public ProcessDefinition(ProcessEngine processEngine, Process process, String name, Boolean active) {
        this.processEngine = processEngine;
        this.process = process;
        this.name = name;
        this.active = active;
    }

    public <T> ProcessInstance<T> startProcessInstance(T variable) {
        return new ProcessInstance<>(processEngine, this, variable);
    }

    public void suspend() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public static ProcessDefinition of(String str) {
        return ProcessDefinition.builder()
                .name("test")
                .active(true)
                .build();
    }
}
