package io.rapidw.easybpmn.registry;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "deployment")
public class Deployment {

    @Builder
    public Deployment(String name, String model) {
        this.name = name;
        this.model = model;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Boolean active;
//    private List<String> classpathResource;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String model;
}
