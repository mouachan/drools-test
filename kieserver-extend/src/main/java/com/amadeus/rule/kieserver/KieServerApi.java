package com.amadeus.rule.kieserver;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.kie.api.runtime.ExecutionResults;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.KieScannerResource;
import org.kie.server.api.model.KieScannerStatus;
import org.kie.server.api.model.ReleaseId;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.services.api.KieServerExtension;
import org.kie.server.services.impl.KieServerImpl;
import org.kie.server.services.impl.storage.file.KieServerStateFileRepository;

import com.amadeus.rule.model.Fact;

public class KieServerApi {
	private static final String KIE_SERVER_ID = "kie-server-impl-test";
	private static final File REPOSITORY_DIR = new File("target/repository-dir");
	private Logger logger = Logger.getLogger(KieServerApi.class);

	private static KieServerImpl INSTANCE = null;

	public static KieServerImpl get() {
		if (INSTANCE == null) {
			setUp();
			INSTANCE = new KieServerImpl(new KieServerStateFileRepository(REPOSITORY_DIR));
		}
		return INSTANCE;
	}

	public static void setUp() {
		System.setProperty("org.kie.server.id", KIE_SERVER_ID);
		System.setProperty("kie.maven.settings.custom", "/Users/mouachan/.m2/settings.xml");
		System.setProperty("org.kie.server.repo", "/Users/mouachan/.m2/repository");
		System.setProperty("org.jbpm.server.ext.disabled", "true");
		System.setProperty("org.jbpm.ui.server.ext.disabled", "true");
		System.setProperty("org.optaplanner.server.ext.disabled", "true");
		System.setProperty("org.jbpm.case.server.ext.disabled", "true");
		try {
			FileUtils.deleteDirectory(REPOSITORY_DIR);
			FileUtils.forceMkdir(REPOSITORY_DIR);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void destroy() {
		get().destroy();
	}

	public <T> T getExtension(Class<T> extensionType) {
		for (KieServerExtension extension : get().getServerExtensions()) {
			if (extensionType.isAssignableFrom(extension.getClass())) {
				logger.info(extension.getExtensionName());
				return (T) extension;
			}
		}
		return null;
	}

	public void createContainer(String groupId, String artifactId, String version, String containerId, String alias) {
		ReleaseId releaseId = new ReleaseId(groupId, artifactId, version);
		KieContainerResource kieContainerResource = new KieContainerResource(containerId, releaseId);
		kieContainerResource.setContainerAlias(alias);
		KieScannerResource kieScannerResource = new KieScannerResource(KieScannerStatus.STARTED, 20000L);
	    kieContainerResource.setScanner(kieScannerResource);
		ServiceResponse<KieContainerResource> response = get().createContainer(containerId, kieContainerResource);
	}

	public void updateContainer(String groupId, String artifactId, String version, String containerId, String alias) {
		ReleaseId releaseId = new ReleaseId(groupId, artifactId, version);
		KieContainerResource kieContainerResource = new KieContainerResource(containerId, releaseId);
		kieContainerResource.setContainerAlias(alias);
		get().updateContainerReleaseId(containerId, releaseId);
		get().updateScanner(alias, new KieScannerResource(KieScannerStatus.STARTED, 200L));
	}

	public ExecutionResults execute(List<Fact> facts, String containerId) {
		ServiceResponse<ExecutionResults> response = getExtension(ProtoBufDroolsKieServerExtension.class)
				.getAppComponents(ProtoBufDroolsKieContainerCommandServiceImpl.class).callContainer(containerId, facts);
		logger.info(response.getMsg());
		return response.getResult();
	}
	
	public ExecutionResults executeByAlias(List<Fact> facts, String alias) {
		ServiceResponse<ExecutionResults> response = getExtension(ProtoBufDroolsKieServerExtension.class)
				.getAppComponents(ProtoBufDroolsKieContainerCommandServiceImpl.class).callContainerByAlias(alias, facts);
		logger.info(response.getMsg());
		return response.getResult();
	}

}
