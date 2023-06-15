package io.rapidw.easybpmn.engine.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class Activity extends FlowNode {

    private Boolean isForCompensation;
//    private LoopCharacteristics loopCharacteristics;
    private List<ResourceRole> resources;
    private SequenceFlow defaultFlow;
//    private InputOutputSpecification ioSpecification;
    private List<Property> properties;
//    private List<BoundaryEvent> boundaryEvents;
//    private List<DataAssociation> dataInputAssociations;
//    private List<DataAssociation> dataOutputAssociations;
    private Integer startQuantity = 1;
    private Integer completionQuantity = 1;
}
