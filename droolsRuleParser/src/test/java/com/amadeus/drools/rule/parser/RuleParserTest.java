package com.amadeus.drools.rule.parser;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.apache.log4j.Logger;
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

	public void setUp(String filepath) {
		noloop = new RuleAttribute("noloop", null);
		dialect = new RuleAttribute("dialect", "mvel");
		salience = new RuleAttribute("salience", "10000");
	}

	@Test
	public void testParserFromDrlFile() {
		logger.info("Test parse from drl file");
		RuleParser rp = new RuleParser();
		rp.readDrlFromFile(path + "rules.drl");
		RuleSet rsparsed = rp.parseRuleSet();
		String rstojson = rp.convertObjectToJson(rsparsed);
		RuleSet jsontors = rp.convertJsonToObject(rstojson, RuleSet.class);
		assertEquals(rsparsed, jsontors);
	}
	@Test
	public void parseFromString(){
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
			
			logger.info("Test parse from drl string");
			RuleParser rp = new RuleParser();
			rp.readDrlFromString(drl);
			RuleSet rsparsed = rp.parseRuleSet();
			String rstojson = rp.convertObjectToJson(rsparsed);
			RuleSet jsontors = rp.convertJsonToObject(rstojson, RuleSet.class);
			assertEquals(rsparsed, jsontors);
			rp.objectToDrlString(rsparsed);
	}

}
