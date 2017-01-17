package com.amadeus.rule.kieserver.test;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.amadeus.rule.kieserver.KieServerApi;
import com.amadeus.rule.model.Fact;

public class KieServerApiTest {
	private Logger logger = Logger.getLogger(KieServerApiTest.class);

	

	
	@Test
	public void testAlias() throws InterruptedException{
		KieServerApi api = new KieServerApi();		
		api.createContainer("com.sample", "RuleTest", "1.0.0-SNAPSHOT","sample_1","sample");
		//TimeUnit.SECONDS.sleep(10);
		Fact fact = new Fact();
		fact.setNamespace("com.amadeus.droolsfeature.rules");
		fact.setName("BzrMarketDeclare");
		fact.getAttributes().put("airport", "LIL");
		fact.setIdentifier("market");
		ArrayList<Fact> facts = new ArrayList<Fact>();
		facts.add(fact);
		api.executeByAlias(facts, "sample");
		api.createContainer("com.sample", "RuleTest", "2.0.0-SNAPSHOT","sample_2","sample");
		fact = new Fact();
		fact.setNamespace("com.amadeus.droolsfeature.rules");
		fact.setName("BzrMarketDeclare");
		fact.getAttributes().put("airport", "POSTER");
		fact.setIdentifier("market");
		facts = new ArrayList<Fact>();
		facts.add(fact);
		api.executeByAlias(facts, "sample");

		api.destroy();
	}

}
