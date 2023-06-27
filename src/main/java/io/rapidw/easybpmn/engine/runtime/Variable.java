package io.rapidw.easybpmn.engine.runtime;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rapidw.easybpmn.ProcessEngineException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Variable {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(name = "class")
    private String clazz;
    @Column(name = "data")
    private String data;

    public Variable(ObjectMapper objectMapper, Object variable) {
        this.clazz = variable.getClass().getName();
        this.data = serialize(objectMapper, variable);
    }

    public static String serialize(ObjectMapper objectMapper, Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new ProcessEngineException("failed to serialize variable", e);
        }
    }

    public <T> T deserialize(ObjectMapper objectMapper,  Class<T> tClass) {
        if (clazz.equals(tClass.getName())) {
            try {
                return objectMapper.readValue(data, tClass);
            } catch (Exception e) {
                throw new ProcessEngineException("failed to deserialize variable", e);
            }
        } else {
            throw new ProcessEngineException("variable type mismatch");
        }
    }

    public Object deserialize(ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(data, Class.forName(clazz));
        } catch (Exception e) {
            throw new ProcessEngineException("failed to deserialize variable", e);
        }
    }
}
