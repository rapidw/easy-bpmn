package io.rapidw.easybpmn.utils;

import io.rapidw.easybpmn.engine.model.FlowElement;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class FlowElementTuple {
    @Getter
    private FlowElement model;
    @Getter
    private io.rapidw.easybpmn.engine.serialization.FlowElement serialization;

}
