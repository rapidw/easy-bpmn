package io.rapidw.easybpmn.process;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class Variable {
   public abstract Variable deepCopy();
}
