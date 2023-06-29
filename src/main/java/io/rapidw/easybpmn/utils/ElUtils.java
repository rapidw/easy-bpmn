package io.rapidw.easybpmn.utils;

import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.runtime.Execution;
import jakarta.el.ELException;
import jakarta.el.ELManager;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.slf4j.Logger;

@Slf4j
public class ElUtils {
    public static Logger logger = log;

    public static Boolean evaluateToBoolean(Execution execution, String conditionExpression, Object variableObject) {
        return evaluateExpression(execution, conditionExpression, variableObject, Boolean.class);
    }

    public static <T> T evaluateExpression(Execution execution, String conditionExpression, Object variableObject, Class<T> clazz) {
        val manager = new ELManager();
        manager.defineBean("execution", execution);
        manager.defineBean("processInstance", execution.getProcessInstance());
        manager.defineBean("variable", variableObject);
        manager.importStatic("io.rapidw.easybpmn.utils.ElUtils.logger");
        val context = manager.getELContext();
        try {
            return ELManager.getExpressionFactory().createValueExpression(context, conditionExpression, clazz).getValue(context);
        } catch (ELException e) {
            throw new ProcessEngineException(e);
        }
    }
}
