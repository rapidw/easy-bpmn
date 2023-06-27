package io.rapidw.easybpmn.engine.repository;

import com.google.common.collect.Maps;
import io.rapidw.easybpmn.engine.runtime.ProcessDefinition;

import java.util.Map;

public class ProcessDefinitionService  {
    private Map<Long, ProcessDefinition> processDefinitionMap = Maps.newConcurrentMap();

    public ProcessDefinitionService() {

    }

    public void put(Long id, ProcessDefinition processDefinition) {
        processDefinitionMap.put(id, processDefinition);
    }

    public ProcessDefinition get(Long id) {
        return processDefinitionMap.get(id);

    }
}
