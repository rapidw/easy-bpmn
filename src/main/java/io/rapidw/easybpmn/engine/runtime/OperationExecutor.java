package io.rapidw.easybpmn.engine.runtime;

import io.rapidw.easybpmn.ProcessEngine;
import io.rapidw.easybpmn.engine.runtime.operation.Operation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class OperationExecutor {
    private final ProcessEngine processEngine;
    private final ExecutorService executorService;

    public OperationExecutor(ProcessEngine processEngine) {
        this.processEngine = processEngine;
        val myThreadFactory = new MyThreadFactory(processEngine.getEntityManagerFactory());
        this.executorService = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), myThreadFactory) {
            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                super.beforeExecute(t, r);
                log.debug("EXECUTING OPERATION {}", r.getClass().getSimpleName());
                EntityManager entityManager = processEngine.getEntityManagerFactory().createEntityManager();
                ThreadLocal<EntityManager> entityManagerThreadLocal = new ThreadLocal<>();
                entityManagerThreadLocal.set(entityManager);
            }

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);

                log.debug("FINISHED EXECUTING OPERATION {}", r.getClass().getSimpleName());
                if (t != null) {
                    log.error("uncaught exception in thread {}", Thread.currentThread().getName(), t);
                }
                if (r instanceof FutureTask<?> futureTask) {
                    try {
                        val get = futureTask.get();
                        if (get instanceof Throwable throwable) {
                            log.error("uncaught exception in thread {}", Thread.currentThread().getName(), throwable);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        };
    }

    public void addOperation(Operation operation) {
        this.executorService.submit(() -> {
            log.debug("EXECUTING OPERATION {}", operation.getClass().getSimpleName());
            operation.execute(this.processEngine);
        });
    }

    private static class MyThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final EntityManagerFactory entityManagerFactory;

        public MyThreadFactory(EntityManagerFactory entityManagerFactory) {
            this.entityManagerFactory = entityManagerFactory;
        }

        @Override
        public Thread newThread(Runnable runnable) {
            val thread = new Thread(() -> {

                runnable.run();
            });
            thread.setName("operation-executor-" + threadNumber.getAndIncrement());
            thread.setUncaughtExceptionHandler((t, e) -> log.error("uncaught exception in thread {}", t.getName(), e));
            return thread;
        }
    }
}
