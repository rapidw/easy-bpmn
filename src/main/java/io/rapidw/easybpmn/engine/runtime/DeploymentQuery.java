package io.rapidw.easybpmn.engine.runtime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeploymentQuery {
    private Integer id;
    private String name;
}
