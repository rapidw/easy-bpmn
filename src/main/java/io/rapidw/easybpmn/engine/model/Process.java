package io.rapidw.easybpmn.engine.model;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 *
 */

@Getter
@Setter
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
    //    private final List<FlowElement> flowElements = Lists.newArrayList();
    // map key: element id
    private Map<String, FlowElement> flowElementMap = Maps.newHashMap();
//    private List<SequenceFlow> sequenceFlows = Lists.newArrayList();
//    private List<>
}
