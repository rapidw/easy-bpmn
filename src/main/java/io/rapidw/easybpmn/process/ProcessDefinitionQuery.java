package io.rapidw.easybpmn.process;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProcessDefinitionQuery {
    private Integer id;
    private String key;

}
