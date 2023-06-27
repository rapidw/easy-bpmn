package io.rapidw.easybpmn.engine.serialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.val;

import java.io.IOException;

public class CandidateEnumDeserializer extends StdDeserializer<Enum<?>> {

    public CandidateEnumDeserializer() {
        super(Enum.class);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Enum<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        val enumClass = (Class<? extends Enum>) ctxt.getAttribute("candidateEnumClass");
        if (enumClass != null) {
            // get enum value from string
            return Enum.valueOf(enumClass, p.getText());
        } else {
            return null;
        }
    }
}
