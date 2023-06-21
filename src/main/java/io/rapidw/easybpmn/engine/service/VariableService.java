package io.rapidw.easybpmn.engine.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.ProcessEngine;
import io.rapidw.easybpmn.engine.Variable;
import io.rapidw.easybpmn.engine.repository.VariableRepository;
import lombok.val;

public class VariableService {
    private final ProcessEngine processEngine;
    private final VariableRepository variableRepository;

    public VariableService(ProcessEngine processEngine) {
        this.processEngine = processEngine;
        this.variableRepository = processEngine.getVariableRepository();
    }

    public Variable create(Object variableObject) {
        val variable = new Variable(this.processEngine.getObjectMapper(), variableObject);
        this.variableRepository.persist(variable);
        return variable;
    }




}
