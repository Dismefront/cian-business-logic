package org.dismefront.connection;

public class LegacyConnectionFactory {
    public LegacyConnection getConnection() {
        return new LegacyConnectionImpl();
    }
}
