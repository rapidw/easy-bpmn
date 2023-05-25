package io.rapidw.easybpmn.process.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Process extends FlowElementContainer implements CallableElement {

    // name in CallableElement is moved to Process to avoid multiple inheritance
    private String name;
//    private ProcessType type;
    private boolean executable = true;
//    private Auditing autiting;
//    private Monitoring monitoring;

    private List<Artifact> artifacts;
    private boolean closed = true;
    private List<Process> supports;
    private List<Property> properties;
    private List<ResourceRole> resources;
//    private List<CorrelationSubscription> correlationSubscriptions;
//    private List<Collaboration> definitionalCollaborationRef;



    private FlowElement initialFlowElement;
//    private List<FlowElement> flowElements;
//    private List<>

    @Builder
    public Process(FlowElement initialFlowElement) {
        this.initialFlowElement = initialFlowElement;
    }
}
