package io.rapidw.easybpmn.engine.serialization;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
public class Definition {
    private String id;
    @Getter(lazy = true)
    private final List<Process> processes = Lists.newArrayList();
}
