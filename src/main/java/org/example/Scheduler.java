package org.example;

public abstract class Scheduler {
    protected StringBuilder executionOrder;

    public abstract void schedule(Process[] processes);

    public String getExecutionOrder() {
        return executionOrder.toString();
    }
}
