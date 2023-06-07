package io.rapidw.easybpmn.engine.runtime;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class ProcessContainer {

    private String processStr;
}
