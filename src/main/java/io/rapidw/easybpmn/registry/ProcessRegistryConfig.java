package io.rapidw.easybpmn.registry;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProcessRegistryConfig {
    private Class<Enum<?>> candidateEnumClass;
}
