package io.rapidw.easybpmn.engine.serialization;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Process {
    private String id;
    private String name;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = StartEvent.class, name = "StartEvent"),
        @JsonSubTypes.Type(value = UserTask.class, name = "UserTask"),
        @JsonSubTypes.Type(value = EndEvent.class, name = "EndEvent")
    })
    private final List<FlowElement> flowElements = Lists.newArrayList();

    private final List<SequenceFlow> sequenceFlows = Lists.newArrayList();
}
