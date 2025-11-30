package com.rosreestr;

import jakarta.resource.ResourceException;
import jakarta.resource.spi.ActivationSpec;
import jakarta.resource.spi.BootstrapContext;
import jakarta.resource.spi.ResourceAdapter;
import jakarta.resource.spi.endpoint.MessageEndpointFactory;

import javax.transaction.xa.XAResource;
import java.io.Serializable;

public class RRResourceAdapter implements ResourceAdapter, Serializable {

    @Override
    public void start(BootstrapContext ctx) {
        System.out.println("RRResourceAdapter started");
    }

    @Override
    public void stop() {
        System.out.println("RRResourceAdapter stopped");
    }

    @Override
    public void endpointActivation(MessageEndpointFactory factory,
                                   ActivationSpec spec) throws ResourceException {
        // not needed for outbound-only adapter
    }

    @Override
    public void endpointDeactivation(MessageEndpointFactory factory,
                                     ActivationSpec spec) {
        // not needed
    }

    @Override
    public XAResource[] getXAResources(ActivationSpec[] specs) {
        return new XAResource[0];
    }

}
