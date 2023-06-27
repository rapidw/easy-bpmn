package io.rapidw.easybpmn.engine.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Activity extends FlowNode {
    private Boolean isForCompensation;
    private LoopCharacteristics loopCharacteristics;
    private List<ResourceRole> resources;
    private SequenceFlow defaultFlow;
    private InputOutputSpecification ioSpecification;
    private List<Property> properties;
    private List<BoundaryEvent> boundaryEvents;
    private List<DataInputAssociation> dataInputAssociations;
    private List<DataOutputAssociation> dataOutputAssociations;
    private Integer startQuantity = 1;
    private Integer completionQuantity = 1;


    // todo
    // skip this activity when expression evaluates to true
    private String skipExpression;
}
