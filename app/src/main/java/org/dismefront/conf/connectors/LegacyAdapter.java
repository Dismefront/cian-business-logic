package org.dismefront.conf.connectors;

import org.dismefront.connection.LegacyConnection;
import org.dismefront.connection.LegacyConnectionFactory;
import org.springframework.stereotype.Component;


@Component
public class LegacyAdapter {

    private final LegacyConnectionFactory factory;

    public LegacyAdapter() {
        this.factory = new LegacyConnectionFactory();
    }

    public String transfer() throws Exception {
        try (LegacyConnection conn = factory.getConnection()) {
            return conn.ping();
        }
    }

}
