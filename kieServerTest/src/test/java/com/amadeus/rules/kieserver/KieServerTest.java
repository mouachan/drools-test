package com.amadeus.rules.kieserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.lang.DRL5Expressions.neg_operator_key_return;
import org.drools.core.command.runtime.BatchExecutionCommandImpl;
import org.drools.core.command.runtime.rule.FireAllRulesCommand;
import org.drools.core.command.runtime.rule.InsertObjectCommand;
import org.drools.core.runtime.impl.ExecutionResultImpl;
import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.definition.type.FactType;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.executor.api.ExecutionResults;
import org.kie.scanner.MavenRepository;
import org.kie.server.api.KieServerConstants;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.ReleaseId;
import org.kie.server.services.drools.RulesExecutionService;
import org.kie.server.services.impl.KieServerImpl;
import org.kie.server.services.impl.storage.file.KieServerStateFileRepository;

import com.amadeus.droolsfeature.rules.BzrMarketDeclare;

public class KieServerTest {
	private static final File REPOSITORY_DIR = new File("target/repository-dir");
	private static final String KIE_SERVER_ID = "kie-server-impl-test";
	private static final String GROUP_ID = "org.kie.server.test";
	private static final String DEFAULT_VERSION = "1.0.0.Final";

	private KieServerImpl kieServer;
	private org.kie.api.builder.ReleaseId releaseId;
	private Logger logger = Logger.getLogger(KieServerTest.class);

	@Before
	public void setupKieServerImpl() throws Exception {
		System.setProperty("org.kie.server.id", KIE_SERVER_ID);
		FileUtils.deleteDirectory(REPOSITORY_DIR);
		FileUtils.forceMkdir(REPOSITORY_DIR);
		setKieServerProperties();
		kieServer = new KieServerImpl(new KieServerStateFileRepository(REPOSITORY_DIR));
	}

	@After
	public void cleanUp() {
		if (kieServer != null) {
			kieServer.destroy();
		}
	}

	private void setKieServerProperties() {
		// System.setProperty(KieServerConstants.CFG_BYPASS_AUTH_USER, "true");
		// System.setProperty(KieServerConstants.CFG_HT_CALLBACK, "custom");
		// System.setProperty(KieServerConstants.CFG_HT_CALLBACK_CLASS,
		// "org.kie.server.integrationtests.jbpm.util.FixedUserGroupCallbackImpl");
		// System.setProperty(KieServerConstants.CFG_PERSISTANCE_DS,
		// "jdbc/jbpm-ds");
		// System.setProperty(KieServerConstants.CFG_PERSISTANCE_TM,
		// "org.hibernate.service.jta.platform.internal.BitronixJtaPlatform");
		// System.setProperty(KieServerConstants.KIE_SERVER_CONTROLLER,
		// TestConfig.getControllerHttpUrl());
		// System.setProperty(KieServerConstants.CFG_KIE_CONTROLLER_USER,
		// TestConfig.getUsername());
		// System.setProperty(KieServerConstants.CFG_KIE_CONTROLLER_PASSWORD,
		// TestConfig.getPassword());
		// System.setProperty(KieServerConstants.KIE_SERVER_LOCATION,
		// TestConfig.getEmbeddedKieServerHttpUrl());
		// System.setProperty(KieServerConstants.KIE_SERVER_STATE_REPO,
		// "./target");
		// kie server policy settings
		System.setProperty(KieServerConstants.CAPABILITY_BRM, "BRM");
		System.setProperty(KieServerConstants.KIE_SERVER_ACTIVATE_POLICIES, "KeepLatestOnly");
		System.setProperty("policy.klo.interval", "5000");
	}
	
	private void addContainer(){
		ReleaseId releaseId = new ReleaseId("com.sample", "RuleTest", "1.0.0-SNAPSHOT");
		KieContainerResource kieContainerResource = new KieContainerResource("container-test", releaseId);
		kieServer.createContainer(kieContainerResource.getContainerId(), kieContainerResource);
	}

	private void execute() {
		
		RulesExecutionService res = new RulesExecutionService(kieServer.getServerRegistry());
		BatchExecutionCommandImpl bec = new BatchExecutionCommandImpl();

		BzrMarketDeclare market = new BzrMarketDeclare();
		market.setAirport("LIL");
		logger.info("Before execution rules "+market.toString());

		bec.addCommand(new InsertObjectCommand(market, "market"));
		bec.addCommand(new FireAllRulesCommand());
		 org.kie.api.runtime.ExecutionResults exresult = res.call(kieServer.getServerRegistry().getContainer("container-test"), bec);
		BzrMarketDeclare marketbis = (BzrMarketDeclare) exresult.getValue("market");
		logger.info("After execution rules "+market.toString());
		logger.info(marketbis.toString());
	}

	@Test
	public void testKieServer() {
		addContainer();
		execute();
		logger.info(kieServer.getContainerInfo("container-test"));
	}

}
