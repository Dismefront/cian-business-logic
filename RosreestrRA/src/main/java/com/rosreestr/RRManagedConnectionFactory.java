package com.rosreestr;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.*;

import javax.security.auth.Subject;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.Set;

@ConnectionDefinition(
        connectionFactory = RRConnectionFactory.class,
        connectionFactoryImpl = RRConnectionFactoryImpl.class,
        connection = RRConnection.class,
        connectionImpl = RRConnectionImpl.class
)
public class RRManagedConnectionFactory implements ManagedConnectionFactory {

    private String serverHost = "localhost";
    private int serverPort = 8140;

    private transient PrintWriter logWriter;

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getServerPort() {
        return serverPort;
    }

    @Override
    public Object createConnectionFactory(ConnectionManager connectionManager) throws ResourceException {
        return new RRConnectionFactoryImpl(this);
    }

    @Override
    public Object createConnectionFactory() {
        return new RRConnectionFactoryImpl(this);
    }

    @Override
    public ManagedConnection createManagedConnection(Subject subject, ConnectionRequestInfo cri)
            throws ResourceException {
        try {
            Socket socket = new Socket(serverHost, serverPort);
            return new RRManagedConnection(socket, this);
        } catch (Exception e) {
            throw new ResourceException("Could not connect to Rosreestr server", e);
        }
    }

    @Override
    public PrintWriter getLogWriter() {
        return logWriter;
    }

    @Override
    public void setLogWriter(PrintWriter out) {
        this.logWriter = out;
    }

    @Override
    public ManagedConnection matchManagedConnections(Set set, Subject subject, ConnectionRequestInfo cri) {
        return set.isEmpty() ? null : (ManagedConnection) set.iterator().next();
    }

}
