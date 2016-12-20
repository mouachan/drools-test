package com.amadeus.drools.rule.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="ruleset")
@XmlAccessorType(XmlAccessType.FIELD)
public class RuleSet {
	
	@XmlElement(name="namespace")
	private String namespace;
	private List<String> imports = new ArrayList<String>(); 
	@XmlElement(name="functions")
	private List<Function>functions= new ArrayList<Function>();
	@XmlElement(name="rules")
	private List<Rule>rules = new ArrayList<Rule>();

	
	/**
	 * @return the pkg
	 */
	public String getNameSpace() {
		return namespace;
	}

	/**
	 * @param pkg the pkg to set
	 */
	public void setNameSpace(String namespace) {
		this.namespace = namespace;
	}

	/**
	 * @return the imports
	 */
	public List<String> getImports() {
		return imports;
	}

	/**
	 * @param imports the imports to set
	 */
	public void setImports(List<String> imports) {
		this.imports = imports;
	}
	/**
	 * @return the functions
	 */
	public List<Function> getFunctions() {
		return functions;
	}

	/**
	 * @param functions the functions to set
	 */
	public void setFunctions(List<Function> functions) {
		this.functions = functions;
	}

	/**
	 * @return the rules
	 */
	public List<Rule> getRules() {
		return rules;
	}

	/**
	 * @param rules the rules to set
	 */
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	public void addImport(String imp){
		this.imports.add(imp);
	}
	
	public void addFunction(Function function){
		this.functions.add(function);
	}
	
	
	public void addRule(Rule rule){
		this.rules.add(rule);
	}

}
