package com.rosreestr;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.*;

import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class RRManagedConnection implements ManagedConnection {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public RRManagedConnection() throws ResourceException {
        try {
            this.socket = new Socket("localhost", 8140);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new ResourceException("Failed to connect", e);
        }
    }

    public String sendToRR(String msg) {
        out.println(msg);
        try {
            return in.readLine();
        }
        catch (IOException e) {
            return "Error";
        }
    }

    @Override
    public Object getConnection(Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException {
        return new RRConnectionImpl(this);
    }

    @Override
    public void destroy() throws ResourceException {

    }

    @Override
    public void cleanup() throws ResourceException {

    }

    @Override
    public void associateConnection(Object o) throws ResourceException {

    }

    @Override
    public void addConnectionEventListener(ConnectionEventListener connectionEventListener) {

    }

    @Override
    public void removeConnectionEventListener(ConnectionEventListener connectionEventListener) {

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
