package io.rapidw.easybpmn;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "deployment")
public class Deployment {

    @Builder
    public Deployment(String name, String processDefinitionString) {
        this.name = name;
        this.processDefinitionString = processDefinitionString;
    }

    @Id
    @GeneratedValue
    private Integer id;

    private String name;
//    private List<String> classpathResource;

    private String processDefinitionString;
}
