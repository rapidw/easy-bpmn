package io.rapidw.easybpmn.engine.serialization;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class Process {
    private String id;
    private String name;
    private StartEvent startEvent;

    @Getter(lazy = true)
    private final List<SequenceFlow> sequenceFlows = Lists.newArrayList();
}
