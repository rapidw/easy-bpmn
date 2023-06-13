package io.rapidw.easybpmn.engine;

import io.rapidw.easybpmn.engine.runtime.ProcessInstance;
import lombok.RequiredArgsConstructor;
import org.hibernate.CallbackException;
import org.hibernate.Interceptor;
import org.hibernate.type.Type;

@RequiredArgsConstructor
public class SessionFactoryInterceptor implements Interceptor {
    private final ProcessEngine processEngine;

    @Override
    public boolean onLoad(Object entity, Object id, Object[] state, String[] propertyNames, Type[] types) throws CallbackException {
        if (entity instanceof ProcessInstance processInstance) {
            processInstance.setProcessEngine(processEngine);
            return true;
        }
        return Interceptor.super.onLoad(entity, id, state, propertyNames, types);
    }
}
