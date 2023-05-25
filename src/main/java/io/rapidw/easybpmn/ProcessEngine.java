package io.rapidw.easybpmn;

import io.rapidw.easybpmn.process.ProcessDefinition;
import io.rapidw.easybpmn.process.ProcessDefinitionQuery;
import io.rapidw.easybpmn.service.HistoryService;
import io.rapidw.easybpmn.service.RepositoryService;
import io.rapidw.easybpmn.service.RuntimeService;
import io.rapidw.easybpmn.service.TaskService;
import io.rapidw.easybpmn.process.TaskInstance;
import io.rapidw.easybpmn.task.TaskQuery;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Builder
public class ProcessEngine {

    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    @Getter
    private final TaskService taskService;
    private final HistoryService historyService;


    public ProcessEngine getDefaultProcessEngine() {
        return ProcessEngine.builder()
                .repositoryService(new RepositoryService())
                .runtimeService(new RuntimeService())
                .taskService(new TaskService())
                .historyService(new HistoryService())
                .build();
    }

    public ProcessDefinition newProcessDefinition(String processDefinition) {

        repositoryService.deploy(Deployment.builder().processDefinitionString(processDefinition).build());
        return ProcessDefinition.builder().processEngine(this).active(true).build();
    }

    public List<ProcessDefinition> queryProcessDefinition(ProcessDefinitionQuery query) {
        return repositoryService.queryProcessDefinition(query);
    }

    public List<TaskInstance<?, ?>> queryTask(TaskQuery query) {
        return taskService.queryTask(query);
    }

}
