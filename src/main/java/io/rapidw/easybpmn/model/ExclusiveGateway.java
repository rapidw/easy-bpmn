package io.rapidw.easybpmn.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExclusiveGateway extends Gateway {

    private SequenceFlow defaultFlow;
}
