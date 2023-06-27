package io.rapidw.easybpmn.query;

import io.rapidw.easybpmn.engine.common.Candidate;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
public class TaskInstanceQuery {

    private Long id;
    private Long processInstanceId;
    @Singular
    private List<Candidate> candidates;
}
