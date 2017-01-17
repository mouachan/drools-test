package com.amadeus.rules.kieserver.controller;

import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;
import org.kie.server.controller.rest.RestKieServerControllerImpl;
import org.kie.server.controller.rest.RestSpecManagementServiceImpl;

import com.amadeus.rules.kieserver.TestConfig;


public class KieControllerExecutor {

    protected TJWSEmbeddedJaxrsServer controller;

    public void startKieController() {
        if (controller != null) {
            throw new RuntimeException("Kie execution controller is already created!");
        }

        controller = new TJWSEmbeddedJaxrsServer();
        controller.setPort(TestConfig.getControllerAllocatedPort());
        controller.start();
        controller.getDeployment().getRegistry().addSingletonResource(new RestKieServerControllerImpl());
        controller.getDeployment().getRegistry().addSingletonResource(new RestSpecManagementServiceImpl());
    }

    public void stopKieController() {
        if (controller == null) {
            throw new RuntimeException("Kie execution controller is already stopped!");
        }
        controller.stop();
        controller = null;
    }
}
