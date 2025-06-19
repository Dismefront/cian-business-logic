package com.rosreestr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RRConnectionImpl implements RRConnection {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;

    public RRConnectionImpl(String host, int port) throws IOException {
        this.socket = new java.net.Socket(host, port);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
    }

    @Override
    public String send(String message) {
        out.println(message);
        try {
            return in.readLine();
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public void close() {
        try {
            socket.close();
        }
        catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

}
