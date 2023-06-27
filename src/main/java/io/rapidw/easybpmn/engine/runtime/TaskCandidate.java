package io.rapidw.easybpmn.engine.runtime;

import io.rapidw.easybpmn.engine.common.Candidate;
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

}
