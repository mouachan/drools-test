package com.amadeus.rules.kieserver;



import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.kie.server.api.model.KieContainerStatus;
import org.kie.server.api.model.KieScannerStatus;
import org.kie.server.api.model.ReleaseId;
import org.kie.server.controller.api.model.runtime.Container;
import org.kie.server.controller.api.model.spec.Capability;
import org.kie.server.controller.api.model.spec.ContainerConfig;
import org.kie.server.controller.api.model.spec.ContainerSpec;
import org.kie.server.controller.api.model.spec.ProcessConfig;
import org.kie.server.controller.api.model.spec.RuleConfig;
import org.kie.server.controller.api.model.spec.ServerTemplate;
import org.kie.server.controller.api.model.spec.ServerTemplateKey;
import org.kie.server.controller.api.service.RuleCapabilitiesService;
import org.kie.server.controller.api.service.RuntimeManagementService;
import org.kie.server.controller.api.service.SpecManagementService;
import org.kie.server.controller.impl.KieServerInstanceManager;

public abstract class AbstractServiceImplTest {

    protected SpecManagementService specManagementService;
    protected RuleCapabilitiesService ruleCapabilitiesService;
    protected RuntimeManagementService runtimeManagementService;
    protected KieServerInstanceManager kieServerInstanceManager;

    protected ServerTemplate serverTemplate;
    protected Container container;
    protected ContainerSpec containerSpec;

    protected void createServerTemplateWithContainer() {
        serverTemplate = new ServerTemplate();

        serverTemplate.setName("test server");
        serverTemplate.setId(UUID.randomUUID().toString());

        specManagementService.saveServerTemplate(serverTemplate);

        Collection<org.kie.server.controller.api.model.spec.ServerTemplateKey> existing = specManagementService.listServerTemplateKeys();
        assertNotNull(existing);
        assertEquals(1, existing.size());

        Map<Capability, ContainerConfig> configs = new HashMap<Capability, ContainerConfig>();
        RuleConfig ruleConfig = new RuleConfig();
        ruleConfig.setPollInterval(1000l);
        ruleConfig.setScannerStatus(KieScannerStatus.STARTED);

        configs.put(Capability.RULE, ruleConfig);

      /*  ProcessConfig processConfig = new ProcessConfig();
        processConfig.setKBase("defaultKieBase");
        processConfig.setKSession("defaultKieSession");
        processConfig.setMergeMode("MERGE_COLLECTION");
        processConfig.setRuntimeStrategy("PER_PROCESS_INSTANCE");

        configs.put(Capability.PROCESS, processConfig);*/

        containerSpec = new ContainerSpec();
        containerSpec.setId("test container");
        containerSpec.setServerTemplateKey(new ServerTemplateKey(serverTemplate.getId(), serverTemplate.getName()));
        containerSpec.setReleasedId(new ReleaseId("org.kie", "kie-server-kjar", "1.0"));
        containerSpec.setStatus(KieContainerStatus.STOPPED);
        containerSpec.setConfigs(configs);

        specManagementService.saveContainerSpec(serverTemplate.getId(), containerSpec);

        container = new Container();
        container.setServerInstanceId(serverTemplate.getId());
        container.setServerTemplateId(serverTemplate.getId());
        container.setResolvedReleasedId(containerSpec.getReleasedId());
        container.setContainerName(containerSpec.getContainerName());
        container.setContainerSpecId(containerSpec.getId());
        container.setUrl("http://fake.server.net/kie-server");
        container.setStatus(containerSpec.getStatus());

    }
}
