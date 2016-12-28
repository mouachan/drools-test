package com.amadeus.drools.rule.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.amadeus.drools.rule.model.Rule;
import com.amadeus.drools.rule.model.RuleAttribute;
import com.amadeus.drools.rule.model.RuleSet;

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
	
	@Test
	public void testParserByVisitor(){
		logger.info("Parse using visitor");
		RuleParser rp = new RuleParser();
		rp.setUp(path + "rules.drl");
		RuleSet rsparsed = rp.parseRule();
		String rstojson = rp.convertObjectToJson(rsparsed);
		RuleSet jsontors = rp.convertJsonToRuleSet(rstojson);
		assertEquals(rsparsed, jsontors);
	}
}
