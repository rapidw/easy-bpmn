package io.rapidw.easybpmn.process;

import io.rapidw.easybpmn.model.Process;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Entity
@NoArgsConstructor
@Table(name = "process_definition")
public class ProcessDefinition implements HasId {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private Boolean active;

    @Transient
    private Process process;

    @Builder
    public ProcessDefinition(Process process, String name, Boolean active) {
        this.process = process;
        this.name = name;
        this.active = active;
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
