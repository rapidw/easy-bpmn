package io.rapidw.easybpmn.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rapidw.easybpmn.ProcessEngineException;
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

    public <T> T getVariable(ObjectMapper objectMapper,  Class<T> tClass) {
        if (clazz.equals(tClass.getName())) {
            try {
                return objectMapper.readValue(json, tClass);
            } catch (Exception e) {
                throw new ProcessEngineException("failed to deserialize variable", e);
            }
        } else {
            throw new ProcessEngineException("variable type mismatch");
        }
    }
}
