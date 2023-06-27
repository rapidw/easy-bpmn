package io.rapidw.easybpmn.registry;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeploymentQuery {
    private Long id;
    private String name;
}
