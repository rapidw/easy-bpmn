package io.rapidw.easybpmn.engine.serialization;

public class MultiInstanceLoopCharacteristics extends LoopCharacteristics {
    private boolean isSequential;
    private String loopCardinality;
    private String completionCondition;

    // extended fields
    private String collection;
    private String elementVariable;
}
