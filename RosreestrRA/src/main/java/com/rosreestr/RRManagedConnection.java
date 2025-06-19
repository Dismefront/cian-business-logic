package com.rosreestr;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.*;

import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class RRManagedConnection implements ManagedConnection {

    private final RRConnectionImpl connection;
    private final List<ConnectionEventListener> listeners = new ArrayList<>();

    public RRManagedConnection(String host, int port) throws ResourceException {
        try {
            this.connection = new RRConnectionImpl(host, port);
        } catch (Exception e) {
            throw new ResourceException("Failed to connect", e);
        }
    }

    @Override
    public Object getConnection(Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException {
        return connection;
    }

    @Override
    public void destroy() throws ResourceException {
        connection.close();
    }

    @Override
    public void cleanup() throws ResourceException {

    }

    @Override
    public void associateConnection(Object o) throws ResourceException {

    }

    @Override
    public void addConnectionEventListener(ConnectionEventListener connectionEventListener) {
        listeners.add(connectionEventListener);
    }

    @Override
    public void removeConnectionEventListener(ConnectionEventListener connectionEventListener) {
        listeners.remove(connectionEventListener);
    }

    @Override
    public XAResource getXAResource() throws ResourceException {
        return null;
    }

    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {
        return null;
    }

    @Override
    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter printWriter) throws ResourceException {

    }

    @Override
    public PrintWriter getLogWriter() throws ResourceException {
        return null;
    }
}
