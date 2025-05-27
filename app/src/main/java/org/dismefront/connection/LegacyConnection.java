package org.dismefront.connection;

public interface LegacyConnection extends AutoCloseable {
    String ping();
}
