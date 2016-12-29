package com.amadeus.drools.rule.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.drools.compiler.compiler.DrlParser;
import org.drools.compiler.compiler.DroolsParserException;
import org.drools.compiler.lang.descr.PackageDescr;
import org.kie.api.KieServices;
import org.kie.api.io.KieResources;
import org.kie.api.io.Resource;

import com.amadeus.drools.rule.model.Rule;
import com.amadeus.drools.rule.model.RuleSet;

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
	private KieServices kieServices = null;
	private KieResources kieResources = null;
	private DrlParser parser = null;
	
	/**
	 * Parse Drl from file and convert it to package descriptor 
	 * @param filepath
	 */
	public void readDrlFromFile(String filepath) {
		kieServices = KieServices.Factory.get();
		kieResources = kieServices.getResources();
		Resource resource = kieResources.newFileSystemResource(filepath);
		parser = new DrlParser();
		try {
			packageDescr = parser.parse(resource);
		} catch (DroolsParserException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Parse Drl from string and convert it to package descriptor 
	 * @param drl
	 */
	public void readDrlFromString(String drl) {
		kieServices = KieServices.Factory.get();
		kieResources = kieServices.getResources();
		InputStream is = new ByteArrayInputStream(drl.getBytes());
		Resource resource = kieResources.newInputStreamResource(is);
		parser = new DrlParser();
		try {
			packageDescr = parser.parse(resource);
		} catch (DroolsParserException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Visit package descriptor and convert it to ruleset
	 * @return
	 */
	public RuleSet parseRuleSet() {
		logger.debug("Parsing ruleset");
		ruleset = pdrv.visit(packageDescr);
		return ruleset;
	}

	/**
	 * Convert Java object to JSON
	 * 
	 * @param obj
	 * @return JSON string format
	 */
	public String convertObjectToJson(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonInString = mapper.writeValueAsString(obj);
			logger.debug("Json generated from Object " + obj.toString() + "\n" + jsonInString);
			return jsonInString;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Convert JSON to Java object 
	 * TODO only ruleset type is implemented
	 * @param json to convert
	 * @param claz of java object whiched
	 * @return java object
	 */
	public RuleSet convertJsonToObject(String json, Class<?> claz) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (claz.equals(RuleSet.class)) {
				RuleSet ruleset = mapper.readValue(json, RuleSet.class);
				logger.debug("Object of class " + claz.getName() + " generated from json " + json + "\n "
						+ ruleset.toString());
				return ruleset;
			}
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Build String from ruleset
	 * @param ruleset
	 * @return String 
	 */
	public String objectToDrlString(RuleSet ruleset){
		return "\n"+pdrv.buildRules(ruleset);
	}
}
