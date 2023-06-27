package io.rapidw.easybpmn.engine.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.rapidw.easybpmn.engine.common.Candidate;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserTask extends Task {
    private String name;

    private String assignee;
    private List<Candidate> candidates = new ArrayList<>();
}
