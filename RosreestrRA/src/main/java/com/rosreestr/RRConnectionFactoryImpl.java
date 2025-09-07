package com.rosreestr;

public class RRConnectionFactoryImpl implements RRConnectionFactory {

    private final RRManagedConnectionFactory mcf;

    public RRConnectionFactoryImpl(RRManagedConnectionFactory mcf) {
        this.mcf = mcf;
    }

    @Override
    public RRConnection getConnection() {
        try {
            RRManagedConnection mc = (RRManagedConnection) mcf.createManagedConnection(null, null);
            return new RRConnectionImpl(mc);
        } catch (Exception e) {
            throw new RuntimeException("Cannot create connection", e);
        }
    }

}
