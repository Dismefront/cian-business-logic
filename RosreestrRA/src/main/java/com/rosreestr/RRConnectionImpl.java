package com.rosreestr;

public class RRConnectionImpl implements RRConnection {

    private final RRManagedConnection mc;

    public RRConnectionImpl(RRManagedConnection mc) {
        this.mc = mc;
    }

    @Override
    public String send(String message) {
        return this.mc.sendToRR(message);
    }

    @Override
    public void close() {

    }

}
