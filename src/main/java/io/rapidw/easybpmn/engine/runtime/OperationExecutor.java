package io.rapidw.easybpmn.engine.runtime;

import io.rapidw.easybpmn.ProcessEngine;
import io.rapidw.easybpmn.engine.runtime.operation.AbstractOperation;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class OperationExecutor {
    private final ProcessEngine processEngine;
    private final ExecutorService executorService;
    private final ThreadLocal<EntityManager> entityManagerThreadLocal;

    public OperationExecutor(ProcessEngine processEngine) {
        this.processEngine = processEngine;
        this.entityManagerThreadLocal = ThreadLocal.withInitial(() -> processEngine.getEntityManagerFactory().createEntityManager());
        val myThreadFactory = new MyThreadFactory();
        this.executorService = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), myThreadFactory) {

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                val entityManager = entityManagerThreadLocal.get();
                entityManager.close();
                entityManagerThreadLocal.remove();

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

    public void addOperation(AbstractOperation operation) {
        this.executorService.submit(() -> {
            log.debug("EXECUTING OPERATION {}", operation.getClass().getSimpleName());
            operation.execute(this.processEngine, this.entityManagerThreadLocal);
        });
    }

    private static class MyThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);


        public MyThreadFactory() {
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
