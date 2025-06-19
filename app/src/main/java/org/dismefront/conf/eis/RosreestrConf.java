package org.dismefront.conf.eis;

import com.rosreestr.RRConnectionFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

@Configuration
public class RosreestrConf {

    @Bean
    public RRConnectionFactoryImpl eisConnectionFactory() {
        Hashtable<String, String> jndiProps = new Hashtable<>();
        jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        jndiProps.put(Context.PROVIDER_URL, "http-remoting://localhost:8076");
        jndiProps.put(Context.SECURITY_PRINCIPAL, "DismefrontUser");
        jndiProps.put(Context.SECURITY_CREDENTIALS, "DismefrontUser");

        RRConnectionFactoryImpl resource = null;

        try {
            Context context = new InitialContext(jndiProps);

            resource = (RRConnectionFactoryImpl) context.lookup("jboss/exported/eis/RRConnectionFactoryRemote");

            System.out.println("Successfully looked up: " + resource);
            context.close();
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return resource;
    }

}
