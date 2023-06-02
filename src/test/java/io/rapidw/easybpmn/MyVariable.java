package io.rapidw.easybpmn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rapidw.easybpmn.process.Variable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;

@Data
@EqualsAndHashCode(callSuper = true)
public class MyVariable extends Variable {

    private String str;

    @JsonIgnore
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @SneakyThrows
    public MyVariable deepCopy() {
        return objectMapper.readValue(objectMapper.writeValueAsString(this), MyVariable.class);
    }
}
