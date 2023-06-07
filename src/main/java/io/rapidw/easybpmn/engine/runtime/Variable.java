package io.rapidw.easybpmn.engine.runtime;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Embeddable
public class Variable {

    private String clazz;
    private String json;
}
