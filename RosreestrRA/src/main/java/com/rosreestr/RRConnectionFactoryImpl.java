package com.rosreestr;


import jakarta.resource.spi.ConnectionManager;

public class RRConnectionFactoryImpl implements RRConnectionFactory {

    private final RRManagedConnectionFactory mcf;
    private final ConnectionManager cm;

    public RRConnectionFactoryImpl(RRManagedConnectionFactory mcf, ConnectionManager cm) {
        this.mcf = mcf;
        this.cm = cm;
    }

    public RRConnection getConnection() {
        try {
            return (RRConnection) cm.allocateConnection(mcf, null);
        } catch (Exception e) {
            throw new RuntimeException("Unable to get connection", e);
        }
    }

}
