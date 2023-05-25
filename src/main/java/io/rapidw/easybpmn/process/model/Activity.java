package io.rapidw.easybpmn.process.model;

import java.util.List;

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
