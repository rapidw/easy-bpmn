package io.rapidw.easybpmn.engine.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlowNode extends FlowElement {
    private List<String> incoming;
    private List<String> outgoing;
}
