package com.amadeus.drools.rule.parser;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.amadeus.drools.rule.model.Attribute;
import com.amadeus.drools.rule.model.Rule;
import static org.junit.Assert.*;

public class RuleParserTest {
	String path = System.getProperty("user.dir") + "/src/main/resources/com/amadeus/drools/rule/parser/";
	private static Logger logger = Logger.getLogger(RuleParserTest.class);
	Attribute noloop = null;
	Attribute dialect = null;
	Attribute salience = null;
	ArrayList<Rule> rules = null;

	@Before
	public void setUp() {
		noloop = new Attribute("noloop", null);
		dialect = new Attribute("dialect", "mvel");
		salience = new Attribute("salience", "10000");
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
	public void testParseSimpleRule() {
		RuleParser rp = new RuleParser();
		rp.setUp(path + "rules.drl");
		rules = (ArrayList<Rule>) rp.parseDrlRule();
		for(Rule rule : rules){
			String rulestring = rule.buildRule();
			logger.info(rulestring);
		}

	}

	@Test
	public void testParseSimpleEvalRule() {
		RuleParser rp = new RuleParser();
		rp.setUp(path + "rulesEval.drl");
		rp.parseDrlRule();
	}

}
