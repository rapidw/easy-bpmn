package io.rapidw.easybpmn;

public class ProcessEngineException extends RuntimeException {
    public ProcessEngineException(String message) {
        super(message);
    }
    public ProcessEngineException(Exception e) {
        super(e);
    }
}
