package com.amadeus.drools.rule.parser;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.schema.JsonSchema;
import org.drools.compiler.compiler.DrlParser;
import org.drools.compiler.compiler.DroolsParserException;
import org.drools.compiler.lang.descr.ImportDescr;
import org.drools.compiler.lang.descr.PackageDescr;
import org.drools.compiler.lang.descr.TypeDeclarationDescr;
import org.kie.api.KieServices;
import org.kie.api.io.KieResources;
import org.kie.api.io.Resource;

import com.amadeus.drools.rule.model.Rule;
import com.amadeus.drools.rule.model.RuleSet;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * @author mouachan
 *
 */
public class RuleParser {
	private static Logger logger = Logger.getLogger(RuleParser.class);
	protected String filename;
	protected PackageDescr packageDescr = null;
	protected Rule rule = new Rule();
	protected RuleSet ruleset = new RuleSet();
	protected PackageDescrResourceVisitor pdrv = new PackageDescrResourceVisitor();
	public String lhs = "";

	public void setUp(String filepath) {
		KieServices kieServices = KieServices.Factory.get();
		KieResources kieResources = kieServices.getResources();
		Resource resource = kieResources.newFileSystemResource(filepath);

		DrlParser parser = new DrlParser();
		try {
			packageDescr = parser.parse(resource);
		} catch (DroolsParserException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public RuleSet parseRule() {
		ruleset = pdrv.visit(packageDescr);
		return ruleset;
	}

	/**
	 * TODO Extract declare from package descriptor implement decalre in the
	 * model
	 */
	private void parseDeclare() {
		for (TypeDeclarationDescr declareDescr : packageDescr.getTypeDeclarations()) {
			logger.debug(declareDescr.getFullTypeName());
		}
	}

	/**
	 * TODO Extract import from package descriptor
	 */
	private void parseImport() {
		for (ImportDescr importDescr : packageDescr.getImports()) {
			ruleset.addImport(importDescr.getTarget());
		}
	}

	/**
	 * Convert Java object to JSON
	 * 
	 * @param obj
	 * @return
	 */
	public String convertObjectToJson(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonInString = mapper.writeValueAsString(obj);
			return jsonInString;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public RuleSet convertJsonToRuleSet(String json){
		ObjectMapper mapper = new ObjectMapper();
		try {
			RuleSet ruleset =mapper.readValue(json, RuleSet.class);
			return ruleset;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		 
	}

}
