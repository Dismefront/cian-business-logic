package org.dismefront.conf.eis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.Socket;

@Configuration
public class RosreestrConf {

    @Bean
    public Socket eisConnectionFactory() {
        try {
            Socket socket = new Socket("localhost", 8140);
            System.out.println("Connected to Rosreestr on port 8140");
            return socket;
        } catch (IOException e) {
            System.err.println("Failed to connect to Rosreestr on port 8140");
        }
        return null;
    }

}
