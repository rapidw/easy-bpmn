package io.rapidw.easybpmn.engine.model;

import jakarta.el.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Gateway extends FlowNode {

    public enum GatewayDirection {
        UNSPECIFIED,
        MIXED,
        CONVERGING,
        DIVERGING,
        ;
    }

    private GatewayDirection direction;

    protected static CompositeELResolver DEFAULT_READ_ONLY_RESOLVER = new CompositeELResolver();
    protected static CompositeELResolver DEFAULT_READ_WRITE_RESOLVER = new CompositeELResolver();

    static {
        DEFAULT_READ_ONLY_RESOLVER.add(new ArrayELResolver(true));
        DEFAULT_READ_ONLY_RESOLVER.add(new ListELResolver(true));
        DEFAULT_READ_ONLY_RESOLVER.add(new MapELResolver(true));
        DEFAULT_READ_ONLY_RESOLVER.add(new ResourceBundleELResolver());
        DEFAULT_READ_ONLY_RESOLVER.add(new BeanELResolver(true));

        DEFAULT_READ_WRITE_RESOLVER.add(new ArrayELResolver(false));
        DEFAULT_READ_WRITE_RESOLVER.add(new ListELResolver(false));
        DEFAULT_READ_WRITE_RESOLVER.add(new MapELResolver(false));
        DEFAULT_READ_WRITE_RESOLVER.add(new ResourceBundleELResolver());
        DEFAULT_READ_WRITE_RESOLVER.add(new BeanELResolver(false));
    }
}
