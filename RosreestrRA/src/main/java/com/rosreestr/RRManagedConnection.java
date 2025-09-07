package com.rosreestr;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.*;

import javax.security.auth.Subject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RRManagedConnection implements ManagedConnection {

    private final Socket socket;
    private final RRManagedConnectionFactory mcf;

    public RRManagedConnection(Socket socket, RRManagedConnectionFactory mcf) {
        this.socket = socket;
        this.mcf = mcf;
    }

    public String send(String request) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println(request);
        return in.readLine();
    }

    @Override
    public Object getConnection(Subject subject, ConnectionRequestInfo cri) {
        return new RRConnectionImpl(this);
    }

    @Override
    public void destroy() throws ResourceException {
        try {
            socket.close();
        } catch (IOException e) {
            throw new ResourceException(e);
        }
    }

    @Override
    public void cleanup() {}

    @Override
    public void associateConnection(Object o) {}

    @Override
    public void addConnectionEventListener(ConnectionEventListener listener) {}

    @Override
    public void removeConnectionEventListener(ConnectionEventListener listener) {}

    @Override
    public ManagedConnectionMetaData getMetaData() {
        return new ManagedConnectionMetaData() {
            @Override
            public String getEISProductName() throws ResourceException {
                return "Rosreestr";
            }

            @Override
            public String getEISProductVersion() throws ResourceException {
                return "1.0.0";
            }

            @Override
            public int getMaxConnections() throws ResourceException {
                return 999;
            }

            @Override
            public String getUserName() throws ResourceException {
                return "Dismefront";
            }
        };
    }

    @Override
    public javax.transaction.xa.XAResource getXAResource() {
        return null; // non-XA
    }

    @Override
    public LocalTransaction getLocalTransaction() throws ResourceException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) {}

    @Override
    public PrintWriter getLogWriter() {
        return null;
    }

}
