package com.amadeus.rules.kieserver;




import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Stream;

import org.kie.server.api.KieServerConstants;
import org.kie.server.router.KieServerRouter;
import org.kie.server.router.KieServerRouterConstants;

public class KieServerRouterExecutor {

    protected KieServerRouter router;
    private File repository;

    public void startKieRouter() {
        if (!TestConfig.startRouter()) {
            return;
        }

        if (router != null) {
            throw new RuntimeException("Kie server router is already created!");
        }
        // setup repository for config of router
        repository = new File("target/router-repo");
        repository.mkdirs();
        System.setProperty(KieServerRouterConstants.ROUTER_REPOSITORY_DIR, repository.getAbsolutePath());
        // setup and start router
        Integer port = getRouterPort();
        router = new KieServerRouter();
        router.start("localhost", port);
        if (System.getProperty(KieServerConstants.KIE_SERVER_ROUTER) == null) {
            System.setProperty(KieServerConstants.KIE_SERVER_ROUTER, "http://localhost:" + port);
        }
    }

    public void stopKieRouter() {
        if (!TestConfig.startRouter()) {
            return;
        }
        if (router == null) {
            throw new RuntimeException("Kie execution controller is already stopped!");
        }
        // stop router and remove its config
        System.clearProperty(KieServerRouterConstants.ROUTER_REPOSITORY_DIR);
        router.stop(true);
        router = null;
        Stream.of(repository.listFiles()).forEach(f-> f.delete());
        repository.delete();
    }

    protected Integer getRouterPort() {
        String routerUrl = System.getProperty(KieServerConstants.KIE_SERVER_ROUTER);
        if (routerUrl == null) {
            return TestConfig.getRouterAllocatedPort();
        }

        try {
            URL url = new URL(routerUrl);
            return url.getPort();
        } catch (MalformedURLException e) {
            return TestConfig.getRouterAllocatedPort();
        }

    }
}
