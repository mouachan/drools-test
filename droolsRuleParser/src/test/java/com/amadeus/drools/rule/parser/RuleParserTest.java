package com.amadeus.drools.rule.parser;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.drools.compiler.lang.descr.PackageDescr;
import org.junit.Test;

import com.amadeus.drools.rule.model.Rule;
import com.amadeus.drools.rule.model.RuleAttribute;
import com.amadeus.drools.rule.model.RuleSet;

public class RuleParserTest {
	private static Logger logger = Logger.getLogger(RuleParserTest.class);
	String path = System.getProperty("user.dir") + "/src/main/resources/com/amadeus/drools/rule/parser/";
	RuleAttribute noloop = null;
	RuleAttribute dialect = null;
	RuleAttribute salience = null;
	ArrayList<Rule> rules = null;
	String drl = "package com.amadeus.droolsfeature.rules\n" 
		    +"import java.util.List;\n"
			+"declare  BzrFlightDeclare\n"
						+"number : int\n"
			    		+"airline : String\n" 
			    		+"matchList : java.util.ArrayList\n" 
			+"end\n"
			+"declare  BzrFlightMatchDeclare\n"
					    +" name : String\n" 
			    		+" lom : int\n"
			+"end\n"
			+"declare  Booking\n"
					+" @release( 0 )\n"
			    		+"customer : String\n" 
			    		+"flight : BzrFlightDeclare\n" 
			    		+"returnFlight : BzrFlightDeclare\n" 
			+"end\n"
			+"function void addMatch( BzrFlightDeclare k, String keywordName, int levelOfMatch ) {\n"
					+" BzrFlightMatchDeclare match = new BzrFlightMatchDeclare();\n"
			    		+"match.setName(keywordName);\n"
			    		+" match.setLom(levelOfMatch);\n"
			    		+"if (k.getMatchList() == null) k.setMatchList(new java.util.ArrayList());\n" 
			    		+"    k.getMatchList().add(match);\n"
			+"}\n"
			+"function BzrFlightMatchDeclare createBzrFlightMatchDeclare(String name){\n"
					+" BzrFlightMatchDeclare bfmd = new BzrFlightMatchDeclare();\n"
			    		+" bfmd.setName(name);\n"
			    		+" return bfmd;\n"
			+"}\n"
			+"rule \"rule complexe\"\n"
					+"no-loop\n" 
					+"salience 10000\n"
					+"dialect \"mvel\"\n" 
					+"when\n"
						+"a:Booking (this != null , this == \"b\")\n"
						+"or b:Booking (this != null, g:flight != \"toto\")\n"
						+"and c:Booking (this != null, g:flight != \"toto\")"
						+"and d:Booking (this != null, h:flight matches \"titi\")\n"
						+"or e:Booking (this != null, this == \"b\")\n"
					+"then\n"
						+"insert(f);\n"
						+"System.out.println(\"yes\");\n"
					+"end\n";
	
	String drlTemplate= 
			 "template header\n"+
			  "age\n"+
			  "type\n"+
			  "log\n"+
			  "package org.drools.examples.templates;\n"+
			  "template \"cheesefans\"\n"+
				 "rule \"Cheese fans_@{row.rowNumber}\"\n"+
				 "when\n"+
				    "Person(age == @{age})\n"+
				    "Cheese(type == \"@{type}\")\n"+
				 "then\n"+
				    "list.add(\"@{log}\");\n"+
				 "end\n"+
				"end template\n";
	
	public void setUp(String filepath) {
		noloop = new RuleAttribute("noloop", null);
		dialect = new RuleAttribute("dialect", "mvel");
		salience = new RuleAttribute("salience", "10000");
	}
	private boolean assertPkgDescr(PackageDescr pkg1, PackageDescr pkg2){
		if(!pkg1.getNamespace().equals(pkg2.getNamespace()))
				return false;
		if(!pkg1.getImports().equals(pkg2.getImports()))
			return false;
		if(!pkg1.getTypeDeclarations().equals(pkg2.getTypeDeclarations()))
			return false;
		if(!pkg1.getFunctions().equals(pkg2.getFunctions()))
			return false;
		if(!pkg1.getRules().equals(pkg2.getRules()))
			return false;
		return true;
	}
	@Test
	public void testParserFromDrlFile() {
		logger.info("Test parse from drl file");
		RuleParser rp = new RuleParser();
		rp.readDrlFromFile(path + "rules.drl");
		RuleSet ruleset = rp.parseRuleSet();
		PackageDescr pkg = rp.buildDescrFromRuleSet(ruleset);
		assertPkgDescr(pkg, rp.getPackageDescriptor());
		String rstojson = rp.convertObjectToJson(ruleset);
		RuleSet rulesetFromJ = rp.convertJsonToObject(rstojson, RuleSet.class);
		assertEquals(rulesetFromJ, ruleset);
	}
	@Test
	public void testParseFromString(){		
			logger.info("Test parse from drl string");
			RuleParser rp = new RuleParser();
			rp.readDrlFromString(drl);
			RuleSet ruleset = rp.parseRuleSet();
			PackageDescr pkg = rp.buildDescrFromRuleSet(ruleset);
			assertPkgDescr(pkg, rp.getPackageDescriptor());
			String rstojson = rp.convertObjectToJson(ruleset);
			RuleSet rulesetFromJ = rp.convertJsonToObject(rstojson, RuleSet.class);
			assertEquals(rulesetFromJ, ruleset);
	}
	
	@Test
	public void testParseRuleTemplate(){
		RuleParser rp = new RuleParser();
		rp.readTemplate(drlTemplate);
	}

}
