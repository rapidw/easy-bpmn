package io.rapidw.easybpmn.engine.repository;

import com.google.common.collect.Maps;
import io.rapidw.easybpmn.engine.ProcessDefinition;

import java.util.Map;

public class ProcessDefinitionService  {
    private Map<Integer, ProcessDefinition> processDefinitionMap = Maps.newConcurrentMap();

    public ProcessDefinitionService() {

    }

    public void put(Integer id, ProcessDefinition processDefinition) {
        processDefinitionMap.put(id, processDefinition);
    }

    public ProcessDefinition get(Integer id) {
        return processDefinitionMap.get(id);

    }
}
