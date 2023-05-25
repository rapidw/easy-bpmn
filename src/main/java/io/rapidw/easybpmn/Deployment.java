package io.rapidw.easybpmn;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Deployment {

    private Integer id;
    private String name;
//    private List<String> classpathResource;

    private String processDefinitionString;
}
