package com.amadeus.drools.rule.model;

import java.util.ArrayList;
import java.util.List;

public class Rule {
	
	private String name;
	private List<Constraint>constraints = new ArrayList<Constraint>();
	private List<Consequence>consequences = new ArrayList<Consequence>();
	private List<Attribute> attributes = new ArrayList<Attribute>();


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Constraint> getConstraints() {
		return constraints;
	}
	public void setConstraints(List<Constraint> constraints) {
		this.constraints = constraints;
	}
	public List<Consequence> getConsequences() {
		return consequences;
	}
	public void setConsequences(List<Consequence> consequences) {
		this.consequences = consequences;
	}
	public List<Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	public void addAttribute(Attribute attr) {
		if(attr!=null)
			this.attributes.add(attr);	
	}

	
}
