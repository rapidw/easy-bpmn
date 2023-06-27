package io.rapidw.easybpmn.engine.runtime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.rapidw.easybpmn.engine.serialization.CandidateEnumDeserializer;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TaskCandidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TaskInstance taskInstance;

    @Embedded
    private Candidate candidate;

    @Embeddable
    @Data
    public static class Candidate {
        private String name;
        @JsonDeserialize(using = CandidateEnumDeserializer.class)
        private Enum<?> type;
    }
}
