package io.rapidw.easybpmn;

import io.rapidw.easybpmn.process.ProcessDefinition;
import io.rapidw.easybpmn.service.HistoryService;
import io.rapidw.easybpmn.service.RepositoryService;
import io.rapidw.easybpmn.service.RuntimeService;

public class ProcessEngine {

    public ProcessEngine() {
    }

    public ProcessDefinition getProcessDefinitionByKey(String key) {
        return new ProcessDefinition();
    }

}
