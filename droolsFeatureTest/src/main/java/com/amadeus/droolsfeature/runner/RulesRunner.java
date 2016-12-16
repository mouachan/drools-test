package com.amadeus.droolsfeature.runner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.definition.type.FactType;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.DecisionTableConfiguration;
import org.kie.internal.builder.DecisionTableInputType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;

import com.amadeus.droolsfeature.listener.RulesAgendaListener;

public class RulesRunner {
	private static String path = null;
	protected static RulesAgendaListener agendaEventListener = null;
	private static Logger logger = Logger.getLogger(RulesRunner.class);
	static KieBase kBase = null;
	static KieSession kSession = null;

	public static void initialisation(String fileName) {
		path = System.getProperty("user.dir") + "/src/main/resources/com/amadeus/droolsfeature/rules/";
		KieServices kieServices = KieServices.Factory.get();
		// Create File System services
		KieFileSystem kFileSystem = kieServices.newKieFileSystem();
		Resource resource = null;
		File file = new File(path + fileName);
		logger.info(path+fileName);
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
	
	public void loadRule(){
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

	public static Object createFact(String pkgName, String factTypeName, HashMap<String, Object> attributes) {
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
	
	public Object getAttribute(Object fact,String pkgName, String factTypeName){
		FactType factType = kBase.getFactType(pkgName, factTypeName);
		Object attribute = factType.get(fact, "flight");
		return attribute;
	}

	public static void runRule(List<Object> objects) {
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

	public void runDt() {
		path = System.getProperty("user.dir") + "/src/main/resources/com/amadeus/dtables/";
		DecisionTableConfiguration dtableconfiguration = KnowledgeBuilderFactory.newDecisionTableConfiguration();
		dtableconfiguration.setInputType(DecisionTableInputType.XLS);
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

		kbuilder.add(org.kie.internal.io.ResourceFactory.newClassPathResource(path + "ruleTemplate.xls", getClass()),
				ResourceType.DTABLE, dtableconfiguration);
	}

	public static void main(String[] args) {
		//object to insert to the WM
		ArrayList<Object> objects = new ArrayList<Object>();
		//load rules 
		initialisation("groupF.drl");
	
		//attributes 
		HashMap<String, Object>attributes= new HashMap<String, Object>();
		// set attributes of BzrFlightDeclare
		attributes.put("airline", "1A");
		attributes.put("lom", "5");
		attributes.put("matchList", new ArrayList<Object>());
		Object flight = createFact("com.amadeus.droolsfeature.rules", "BzrFlightDeclare", attributes);
		// clear map of attributes
		attributes.clear();
		//set attributes of Booking
		attributes.put("customer", "Bob");
		attributes.put("lom", "5");
		attributes.put("flight",flight);
		Object booking = createFact("com.amadeus.droolsfeature.rules", "Booking", attributes);
		objects.add(booking);
//		objects.add(getAttribute(booking, "com.amadeus.droolsfeature.rules", "flight"));
		runRule(objects);
	}
}
