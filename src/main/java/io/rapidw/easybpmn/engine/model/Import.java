package io.rapidw.easybpmn.engine.model;

import lombok.Data;

@Data
public class Import {
    private String namespace;
    private String location;
    private String importType;
}
