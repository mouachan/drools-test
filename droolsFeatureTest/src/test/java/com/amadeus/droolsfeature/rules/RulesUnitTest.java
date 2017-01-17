package com.amadeus.droolsfeature.rules;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.drools.compiler.compiler.DrlParser;
import org.drools.compiler.compiler.DroolsParserException;
import org.drools.compiler.compiler.xml.XmlDumper;
import org.drools.compiler.lang.DrlDumper;
import org.drools.compiler.lang.descr.AndDescr;
import org.drools.compiler.lang.descr.BaseDescr;
import org.drools.compiler.lang.descr.ExprConstraintDescr;
import org.drools.compiler.lang.descr.PackageDescr;
import org.drools.compiler.lang.descr.PatternDescr;
import org.drools.compiler.lang.descr.RuleDescr;
import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.definition.type.FactType;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.conf.LanguageLevelOption;

import com.amadeus.droolsfeature.listener.RulesAgendaListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RulesUnitTest {
	private String path = null;
	protected RulesAgendaListener agendaEventListener = null;
	private static Logger logger = Logger.getLogger(RulesUnitTest.class);
	KieBase kBase = null;
	KieSession kSession = null;

	public void initialisation(String fileName) {
		path = System.getProperty("user.dir") + "/src/main/resources/com/amadeus/droolsfeature/rules/";
		KieServices kieServices = KieServices.Factory.get();
		// Create File System services
		KieFileSystem kFileSystem = kieServices.newKieFileSystem();
		Resource resource = null;
		File file = new File(path + fileName);
		logger.info(path + fileName);
		if (fileName.substring(fileName.indexOf('.') + 1, fileName.length()).equals("drl"))
			resource = kieServices.getResources().newFileSystemResource(file).setResourceType(ResourceType.DRL);
		else
			resource = kieServices.getResources().newFileSystemResource(file).setResourceType(ResourceType.DTABLE);
		kFileSystem.write(resource);

		KieBuilder kbuilder = kieServices.newKieBuilder(kFileSystem);
		logger.info(kieServices.getRepository().getDefaultReleaseId());
		kbuilder.buildAll();
		if (kbuilder.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
			logger.error(kbuilder.getResults().getMessages());
			throw new RuntimeException("Build time Errors: " + kbuilder.getResults().toString());
		}
		KieContainer kContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
		kSession = kContainer.newKieSession();
		kBase = kSession.getKieBase();
		logger.info(kContainer.getReleaseId().getVersion());
	}

	public void loadRule() {
		KieServices kieServices = KieServices.Factory.get();
		KieContainer kContainer = kieServices.newKieClasspathContainer();
		kBase = kContainer.getKieBase("kbase1");
		kSession = kContainer.newKieSession("ksession1");
	}

	public Object createFlight(String name) {
		// get the declared FactType
		FactType flightType = kBase.getFactType("com.amadeus.droolsfeature.rules", "Flight");

		// handle the type as necessary:
		// create instances:
		Object flight = null;
		try {
			flight = flightType.newInstance();
			// set attributes values
			// flightType.set(flight, "name", name);

		} catch (InstantiationException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flight;

	}

	public Object createCustomer(String name) {
		// get the declared FactType
		FactType customerType = kBase.getFactType("com.amadeus.droolsfeature.rules", "Customer");

		// handle the type as necessary:
		// create instances:
		Object customer = null;
		try {
			customer = customerType.newInstance();
			// set attributes values
			customerType.set(customer, "name", name);

		} catch (InstantiationException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return customer;

	}

	public Object createFact(String pkgName, String factTypeName, HashMap<String, Object> attributes) {
		Object fact = null;
		FactType factType = kBase.getFactType(pkgName, factTypeName);
		try {
			fact = factType.newInstance();
			for (String attributeName : attributes.keySet()) {
				factType.set(fact, attributeName, attributes.get(attributeName));
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return fact;
	}

	public Object getAttribute(Object fact, String pkgName, String factTypeName) {
		FactType factType = kBase.getFactType(pkgName, factTypeName);
		Object attribute = factType.get(fact, "flight");
		return attribute;
	}

	public void runRule(List<Object> objects) {
		agendaEventListener = new RulesAgendaListener();
		kSession.addEventListener(agendaEventListener);
		for (Object object : objects)
			kSession.insert(object);
		kSession.fireAllRules();
		kSession.dispose();
	}

	public void runRule(String fileName, String ruleFlowGroup, Object object) {
		KieServices kieServices = KieServices.Factory.get();
		// Create File System services
		KieFileSystem kFileSystem = kieServices.newKieFileSystem();
		Resource resource = null;
		File file = new File(path + fileName);
		if (fileName.substring(fileName.indexOf('.') + 1, fileName.length()).equals("drl"))
			resource = kieServices.getResources().newFileSystemResource(file).setResourceType(ResourceType.DRL);
		else
			resource = kieServices.getResources().newFileSystemResource(file).setResourceType(ResourceType.DTABLE);
		kFileSystem.write(resource);

		KieBuilder kbuilder = kieServices.newKieBuilder(kFileSystem);
		kbuilder.buildAll();
		if (kbuilder.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
			throw new RuntimeException("Build time Errors: " + kbuilder.getResults().toString());
		}
		KieContainer kContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
		KieSession kSession = kContainer.newKieSession();
		RulesAgendaListener agendaEventListener = new RulesAgendaListener();
		kSession.addEventListener(agendaEventListener);
		kSession.insert(object);
		// ((InternalAgenda)
		// kSession.getAgenda()).activateRuleFlowGroup(ruleFlowGroup);
		kSession.fireAllRules();
		kSession.dispose();
	}

	/*
	 * public void runDt() { path = System.getProperty("user.dir") +
	 * "/src/main/resources/com/amadeus/dtables/"; DecisionTableConfiguration
	 * dtableconfiguration =
	 * KnowledgeBuilderFactory.newDecisionTableConfiguration();
	 * dtableconfiguration.setInputType(DecisionTableInputType.XLS);
	 * KnowledgeBuilder kbuilder =
	 * KnowledgeBuilderFactory.newKnowledgeBuilder();
	 * 
	 * kbuilder.add(org.kie.internal.io.ResourceFactory.newClassPathResource(
	 * path + "ruleTemplate.xls", getClass()), ResourceType.DTABLE,
	 * dtableconfiguration); }
	 */

	@Test
	public void test() {
		path = System.getProperty("user.dir") + "/src/main/resources/com/amadeus/droolsfeature/rules/";
		KieServices kieServices = KieServices.Factory.get();
		// Create File System services
		KieFileSystem kFileSystem = kieServices.newKieFileSystem();
		logger.info(path);
		path = path + "rules.drl";
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);

			kFileSystem.write("src/main/resources/simple.drl", kieServices.getResources().newInputStreamResource(fis));

			KieBuilder kbuilder = kieServices.newKieBuilder(kFileSystem).buildAll();
			Results results = kbuilder.getResults();
			if (results.hasMessages(Message.Level.ERROR)) {
				System.out.println(results.getMessages());
				throw new IllegalStateException("### errors ###");
			}
			KieContainer kContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
			kSession = kContainer.newKieSession();
			// object to insert to the WM
			ArrayList<Object> objects = new ArrayList<Object>();
			// load rules

			// attributes
			HashMap<String, Object> attributes = new HashMap<String, Object>();
			// set attributes of BzrFlightDeclare
			attributes.put("airline", "1A");
			attributes.put("lom", "5");
			attributes.put("matchList", new ArrayList<Object>());
			Object flight = createFact("com.amadeus.droolsfeature.rules", "BzrFlightDeclare", attributes);
			// clear map of attributes
			attributes.clear();
			// set attributes of Booking
			attributes.put("customer", "Bob");
			attributes.put("lom", "5");
			attributes.put("flight", flight);
			Object booking = createFact("com.amadeus.droolsfeature.rules", "Booking", attributes);
			objects.add(booking);
			// objects.add(getAttribute(booking,
			// "com.amadeus.droolsfeature.rules", "flight"));
			agendaEventListener = new RulesAgendaListener();
			kSession.addEventListener(agendaEventListener);
			for (Object object : objects)
				kSession.insert(object);
			kSession.fireAllRules();
			kSession.dispose();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	@Test
	public void generateXMLFromDrl() {
		DrlParser dp = new DrlParser(LanguageLevelOption.DRL6);

		path = System.getProperty("user.dir") + "/src/main/resources/com/amadeus/droolsfeature/rules/rules2.drl";

		try {
			InputStreamReader is = new InputStreamReader(RulesUnitTest.class.getResourceAsStream("rules2.drl"));
			PackageDescr pdesc = dp.parse(is);

			ObjectMapper mapper = new ObjectMapper();

			// Object to JSON in String
			String jsonInString = mapper.writeValueAsString(pdesc);
			logger.info(jsonInString);

			// PackageDescr pdesc2= (PackageDescr)mapper.readValue(jsonInString,
			// PackageDescr.class);
			// assertEquals(pdesc,pdesc2);

			logger.info(pdesc.getName());
			for (RuleDescr rdesc : pdesc.getRules()) {
				AndDescr lhs = rdesc.getLhs();
				for (BaseDescr bdescr : lhs.getDescrs()) {
					PatternDescr pdescr = (PatternDescr) bdescr;
					logger.info("Object Type "+pdescr.getObjectType());
					for(BaseDescr slc : pdescr.getSlottedConstraints()){
						ExprConstraintDescr ecdescr = (ExprConstraintDescr)slc;
						logger.info(ecdescr.getText());
						String[] constraint=  ecdescr.getText().split(" ");
						for(int i=0 ; i< constraint.length;i++)
							logger.info(constraint[i]);
					}
				}

				// consequence
				logger.info(rdesc.getConsequence());

				for (String keyconsequence : rdesc.getNamedConsequences().keySet())
					logger.info("key : " + keyconsequence);
			}
			final XmlDumper dumper = new XmlDumper();
			DrlDumper drldump = new DrlDumper();
			// logger.info(dumper.dump(pdesc));

		} catch (DroolsParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
