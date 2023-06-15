package io.rapidw.easybpmn.engine.serialization;

import com.fasterxml.jackson.annotation.*;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// todo: tRootElement
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Process {
    private String id;
    private String name;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = StartEvent.class, name = "startEvent"),
        @JsonSubTypes.Type(value = UserTask.class, name = "userTask"),
        @JsonSubTypes.Type(value = EndEvent.class, name = "endEvent"),
        @JsonSubTypes.Type(value = ExclusiveGateway.class, name = "exclusiveGateway"),
        @JsonSubTypes.Type(value = ParallelGateway.class, name = "parallelGateway"),
        @JsonSubTypes.Type(value = InclusiveGateway.class, name = "inclusiveGateway"),
        @JsonSubTypes.Type(value = ServiceTask.class, name = "serviceTask"),
        @JsonSubTypes.Type(value = SequenceFlow.class, name = "sequenceFlow")
    })
    private final List<FlowElement> flowElements = Lists.newArrayList();
}

