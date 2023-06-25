package io.rapidw.easybpmn.engine.runtime;

import jakarta.persistence.*;

@Entity
public class TaskAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TaskInstance taskInstance;

    private String assignee;

    private AssigneeType assigneeType;

    public enum AssigneeType {
        USER,
        GROUP,
        ROLE,
    }
}
