package io.rapidw.easybpmn.utils;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.runtime.Execution;
import lombok.val;

public class GroovyUtils {
    public static Boolean evaluateToBoolean(Execution execution, String expression, Object variableObject) {
        val res = evaluateExpression(execution, expression, variableObject);
        if (res instanceof Boolean b) {
            return b;
        } else {
            throw new ProcessEngineException("expression should evaluate to boolean");
        }
    }

    private static Object evaluateExpression(Execution execution, String expression, Object variableObject) {
        Binding binding = new Binding();
        binding.setProperty("execution", execution);
        binding.setProperty("processInstance", execution.getProcessInstance());
        binding.setProperty("variable", variableObject);
        GroovyShell shell = new GroovyShell(binding);
        return shell.evaluate(expression);
    }
}
