package com.rosreestr;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.ConnectionManager;

public class RRConnectionFactoryImpl {

    private final RRManagedConnectionFactory mcf;
    private final ConnectionManager cxManager;

    public RRConnectionFactoryImpl(RRManagedConnectionFactory mcf, ConnectionManager cxManager) {
        this.mcf = mcf;
        this.cxManager = cxManager;
    }

    public RRConnection getConnection() throws ResourceException {
        if (cxManager != null) {
            return (RRConnection) cxManager.allocateConnection(mcf, null);
        }
        else {
            return (RRConnection) mcf.createManagedConnection(null, null).getConnection(null, null);
        }
    }

}
