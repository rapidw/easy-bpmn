package io.rapidw.easybpmn.engine.serialization;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class Definition {
    private String id;

    private List<Process> processes = Lists.newArrayList();
}
