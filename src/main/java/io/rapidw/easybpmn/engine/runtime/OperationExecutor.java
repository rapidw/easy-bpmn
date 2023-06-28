package io.rapidw.easybpmn.engine.runtime;

import io.rapidw.easybpmn.engine.ProcessEngine;
import io.rapidw.easybpmn.engine.operation.AbstractOperation;
import jakarta.persistence.EntityManager;
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
        val entityManagerThreadLocal = processEngine.getEntityManagerThreadLocal();
        val myThreadFactory = new MyThreadFactory(entityManagerThreadLocal);
        this.executorService = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), myThreadFactory) {

            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                super.afterExecute(r, t);
                if (t == null && r instanceof Future<?> && ((Future<?>) r).isDone()) {
                    try {
                        Object result = ((Future<?>) r).get();
                    } catch (CancellationException ce) {
                        t = ce;
                    } catch (ExecutionException ee) {
                        t = ee.getCause();
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
                if (t != null) {
                    log.error("uncaught exception in thread {}", Thread.currentThread().getName(), t);
                }
            }
        };
    }

    public void addOperation(AbstractOperation operation) {
        log.debug("add operation {}", operation.getClass().getSimpleName());
        this.executorService.submit(() -> {
            operation.execute(this.processEngine);
        });
    }

    private static class MyThreadFactory implements ThreadFactory {
        private final ThreadLocal<EntityManager> entityManagerThreadLocal;
        private final AtomicInteger threadNumber = new AtomicInteger(1);


        public MyThreadFactory(ThreadLocal<EntityManager> entityManagerThreadLocal) {
            this.entityManagerThreadLocal = entityManagerThreadLocal;
        }

        @Override
        public Thread newThread(Runnable runnable) {
            val thread = new Thread(() -> {
//
                runnable.run();
                entityManagerThreadLocal.get().close();
                entityManagerThreadLocal.remove();
            });
            thread.setName("operation-executor-" + threadNumber.getAndIncrement());
            return thread;
        }
    }
}
