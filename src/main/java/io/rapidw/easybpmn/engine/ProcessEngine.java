package io.rapidw.easybpmn.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.operation.AbstractEngineOperation;
import io.rapidw.easybpmn.engine.operation.ContinueProcessEngineOperation;
import io.rapidw.easybpmn.engine.repository.*;
import io.rapidw.easybpmn.registry.DeploymentQuery;
import io.rapidw.easybpmn.registry.ProcessRegistry;
import io.rapidw.easybpmn.task.TaskQuery;
import jakarta.el.ExpressionFactory;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.reflections.Reflections;

import java.util.List;

@Slf4j
public class ProcessEngine {

    //    @Getter
//    private final EntityManagerFactory entityManagerFactory;
    private final SessionFactory sessionFactory;
    @Getter
    private final ObjectMapper objectMapper;
    private final ProcessRegistry processRegistry;
    @Getter
    private final ThreadLocal<EntityManager> entityManagerThreadLocal;
    private final OperationExecutor operationExecutor;
    @Getter
    private final ExpressionFactory expressionFactory;


    //    @Getter
//    private final RuntimeService runtimeService;
    @Getter
    private final TaskInstanceRepository taskInstanceRepository;
    //    private final HistoryService historyService;
    @Getter
    private ProcessInstanceRepository processInstanceRepository;
    @Getter
    private ExecutionRepository executionRepository;
    @Getter
    private VariableRepository variableRepository;

//    @Getter
//    private ProcessInstanceService processInstanceService;
    @Getter
    private ProcessDefinitionService processDefinitionManager;
//    @Getter
//    private VariableService variableService;
//    @Getter
//    private ExecutionService executionService;
//    @Getter
//    private TaskInstanceService taskInstanceService;


    public ProcessEngine(ProcessRegistry processRegistry, ProcessEngineConfig config) {
        val reflections = new Reflections("io.rapidw.easybpmn.engine");
        val entities = reflections.getTypesAnnotatedWith(Entity.class);

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();
        this.sessionFactory = new MetadataSources(registry)
            .addAnnotatedClasses(entities.toArray(new Class[0]))
            .buildMetadata()
            .getSessionFactoryBuilder()
            .applyInterceptor(new SessionFactoryInterceptor(this))
            .build();

//        this.entityManagerFactory = Persistence.createEntityManagerFactory("easy-bpmn");
        this.objectMapper = new ObjectMapper();
//        val jsonSubTypes = reflections.getTypesAnnotatedWith(JsonTypeName.class);
//        jsonSubTypes.forEach(v -> objectMapper.registerSubtypes(new NamedType(v, v.getAnnotation(JsonTypeName.class).value())));
        this.entityManagerThreadLocal = ThreadLocal.withInitial(sessionFactory::createEntityManager);

        this.processRegistry = processRegistry;
        this.operationExecutor = new OperationExecutor(this);
        this.expressionFactory = ExpressionFactory.newInstance();

//        this.runtimeService = new RuntimeService(entityManagerFactory);
        this.taskInstanceRepository = new TaskInstanceRepository(entityManagerThreadLocal);
//        this.historyService = new HistoryService();
        this.processInstanceRepository = new ProcessInstanceRepository(entityManagerThreadLocal);

        this.executionRepository = new ExecutionRepository(entityManagerThreadLocal);
        this.variableRepository = new VariableRepository(entityManagerThreadLocal);

//        this.processInstanceService = new ProcessInstanceService(this);
        this.processDefinitionManager = new ProcessDefinitionService();
//        this.variableService = new VariableService(this);
//        this.executionService = new ExecutionService(this);
//        this.taskInstanceService = new TaskInstanceService(this);
    }

    public void startProcessInstanceById(Integer id, Object variableObject) {
        log.info("start process instance by process id");
        val deployments = this.processRegistry.query(DeploymentQuery.builder().id(id).build());
        if (deployments.size() != 1) {
            throw new ProcessEngineException("invalid deployment id");
        }
        val definition = ProcessDefinition.builder().processEngine(this).deployment(deployments.get(0)).build();
        processDefinitionManager.put(id, definition);

        val transaction = entityManagerThreadLocal.get().getTransaction();
        transaction.begin();
        // save variable
        val variable = new Variable(objectMapper, variableObject);
        variableRepository.persist(variable);

        // create process instance
        val processInstance = new ProcessInstance(id, variable);
        processInstanceRepository.persist(processInstance);

        val execution = Execution.builder()
            .processInstance(processInstance)
            .initialFlowElement(definition.getProcess().getInitialFlowElement())
            .active(true)
            .parent(null)
            .variable(variable)
            .build();

        executionRepository.persist(execution);
        processInstance.getExecutions().add(execution);
//        this.processEngine.getProcessInstanceRepository().merge(processInstance);

        transaction.commit();
        operationExecutor.addOperation(ContinueProcessEngineOperation.builder()
            .executionId(execution.getId())
            .build()
        );
    }

    public void addOperation(AbstractEngineOperation operation) {
        operationExecutor.addOperation(operation);
    }

    public List<TaskInstance> queryTask(TaskQuery taskQuery) {
        return this.taskInstanceRepository.query(taskQuery);
    }

}
