package io.rapidw.easybpmn.engine.common;

import io.rapidw.easybpmn.engine.runtime.Execution;
import io.rapidw.easybpmn.utils.ElUtils;
import io.rapidw.easybpmn.utils.GroovyUtils;

public enum ExpressionType {
    EL {
        @Override
        public Boolean evaluateToBoolean(Execution execution, String expression, Object variableObject) {
            return ElUtils.evaluateToBoolean(execution, expression, variableObject);
        }
    },
    GROOVY {
        @Override
        public Boolean evaluateToBoolean(Execution execution, String expression, Object variableObject) {
            return GroovyUtils.evaluateToBoolean(execution, expression, variableObject);
        }
    },
    ;

    public abstract Boolean evaluateToBoolean(Execution execution, String expression, Object variableObject);
}
