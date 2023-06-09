package io.rapidw.easybpmn.engine.runtime;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Embeddable
public class Variable {

    @Column(name = "variable_class")
    private String clazz;
    @Column(name = "variable_json")
    private String json;
}
