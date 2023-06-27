package io.rapidw.easybpmn.engine.common;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.rapidw.easybpmn.engine.serialization.CandidateEnumDeserializer;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Candidate {
    private String name;
    @JsonDeserialize(using = CandidateEnumDeserializer.class)
    private Enum<?> type;
}
