package io.rapidw.easybpmn.engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.rapidw.easybpmn.ProcessEngineException;
import io.rapidw.easybpmn.engine.operation.AbstractOperation;
import io.rapidw.easybpmn.engine.operation.EnterFlowElementOperation;
import io.rapidw.easybpmn.engine.repository.*;
import io.rapidw.easybpmn.engine.runtime.*;
import io.rapidw.easybpmn.query.TaskInstanceQuery;
import io.rapidw.easybpmn.registry.DeploymentQuery;
import io.rapidw.easybpmn.registry.ProcessRegistry;
import io.rapidw.easybpmn.utils.TransactionUtils;
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

    @Getter
    private final ProcessEngineConfig config;
    @Getter
    private final ObjectMapper objectMapper;
    @Getter
    private final ProcessRegistry processRegistry;
    @Getter
    private final ThreadLocal<EntityManager> entityManagerThreadLocal;
    private final OperationExecutor operationExecutor;
    @Getter
    private final ExpressionFactory expressionFactory;

    @Getter
    private final TaskInstanceRepository taskInstanceRepository;
    @Getter
    private final ProcessInstanceRepository processInstanceRepository;
    @Getter
    private final ExecutionRepository executionRepository;
    @Getter
    private final VariableRepository variableRepository;
    @Getter
    private final TaskAssignmentRepository taskCandidateRepository;

    @Getter
    private final ProcessDefinitionService processDefinitionManager;


    public ProcessEngine(ProcessRegistry processRegistry, ProcessEngineConfig config) {
        this.config = config;
        val reflections = new Reflections("io.rapidw.easybpmn.engine");
        val entities = reflections.getTypesAnnotatedWith(Entity.class);

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure() // configures settings from hibernate.cfg.xml
            .build();
        SessionFactory sessionFactory = new MetadataSources(registry)
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

        this.taskInstanceRepository = new TaskInstanceRepository(entityManagerThreadLocal);
        this.processInstanceRepository = new ProcessInstanceRepository(entityManagerThreadLocal);

        this.executionRepository = new ExecutionRepository(entityManagerThreadLocal);
        this.variableRepository = new VariableRepository(entityManagerThreadLocal);
        this.taskCandidateRepository = new TaskAssignmentRepository(entityManagerThreadLocal);

        this.processDefinitionManager = new ProcessDefinitionService();
    }

    public ProcessInstance startProcessInstanceById(Long id, Object variableObject) {
        log.info("start process instance by process id");
        val deployments = this.processRegistry.query(DeploymentQuery.builder().id(id).build());
        if (deployments.size() != 1) {
            throw new ProcessEngineException("invalid deployment id");
        }
        val definition = ProcessDefinition.builder().processEngine(this).deployment(deployments.get(0)).build();
        processDefinitionManager.put(id, definition);

        return TransactionUtils.callWithTransaction(this, () -> {
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

            operationExecutor.addOperation(EnterFlowElementOperation.builder()
                .executionId(execution.getId())
                .build()
            );
            return processInstance;
        });
    }

    public void addOperation(AbstractOperation operation) {
        operationExecutor.addOperation(operation);
    }

    public List<TaskInstance> queryTask(TaskInstanceQuery taskInstanceQuery) {
        return this.taskInstanceRepository.query(taskInstanceQuery);
    }

}
