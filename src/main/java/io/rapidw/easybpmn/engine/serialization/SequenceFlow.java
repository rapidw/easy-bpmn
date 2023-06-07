package io.rapidw.easybpmn.engine.serialization;

import lombok.Data;

@Data
public class SequenceFlow {
    private String id;
    private String sourceRef;
    private String targetRef;
}
