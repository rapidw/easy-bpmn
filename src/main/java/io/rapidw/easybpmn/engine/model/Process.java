package io.rapidw.easybpmn.engine.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Embeddable
@NoArgsConstructor
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

    // for implementation
    private FlowElement initialFlowElement;
    private List<FlowElement> flowElements;
    // map key: element id
    private Map<String, FlowElement> flowElementMap;
    private List<SequenceFlow> sequenceFlows;
//    private List<>

//    @Builder
    public Process(FlowElement initialFlowElement) {
        this.initialFlowElement = initialFlowElement;
    }
}
