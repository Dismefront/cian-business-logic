package org.dismefront.connection;

public class LegacyConnectionImpl implements LegacyConnection {

    @Override
    public String ping() {
        return "pong";
    }

    @Override
    public void close() throws Exception {

    }
}
