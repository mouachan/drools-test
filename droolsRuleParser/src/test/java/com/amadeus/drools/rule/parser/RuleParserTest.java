package com.amadeus.drools.rule.parser;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.amadeus.drools.rule.model.Rule;
import com.amadeus.drools.rule.model.RuleAttribute;

public class RuleParserTest {
	String path = System.getProperty("user.dir") + "/src/main/resources/com/amadeus/drools/rule/parser/";
	private static Logger logger = Logger.getLogger(RuleParserTest.class);
	RuleAttribute noloop = null;
	RuleAttribute dialect = null;
	RuleAttribute salience = null;
	ArrayList<Rule> rules = null;

	@Before
	public void setUp() {
		noloop = new RuleAttribute("noloop", null);
		dialect = new RuleAttribute("dialect", "mvel");
		salience = new RuleAttribute("salience", "10000");
	}

	private void assertAllRule(String rulename) {
		for (Rule rule : rules) {
			if (rule.getName().equals(rulename)) {
				assertTrue(rule.getAttributes().contains(noloop));
				assertTrue(rule.getAttributes().contains(salience));
				assertTrue(rule.getAttributes().contains(dialect));
			}
		}
	}


	

	
	
	@Test
	public void testParserByVisitor(){
		RuleParser rp = new RuleParser();
		rp.setUp(path + "rules.drl");
		rp.parseRule();
	}
}
