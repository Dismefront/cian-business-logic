package com.rosreestr;

import java.io.IOException;

public class RRConnectionImpl implements RRConnection {

    private final RRManagedConnection mc;

    public RRConnectionImpl(RRManagedConnection mc) {
        this.mc = mc;
    }

    @Override
    public String send(String request) {
        try {
            return mc.send(request);
        } catch (IOException e) {
            throw new RuntimeException("Error sending request to Rosreestr", e);
        }
    }

}
