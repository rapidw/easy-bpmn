package io.rapidw.easybpmn.process.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExclusiveGateway extends Gateway {

    private SequenceFlow defaultFlow;
}