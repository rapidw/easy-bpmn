package io.rapidw.easybpmn.engine.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.rapidw.easybpmn.engine.common.ExpressionType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SequenceFlow extends FlowElement {
    private String sourceRef;
    private String targetRef;

    private String conditionExpression;

    // extended
    private ExpressionType expressionType;
}
