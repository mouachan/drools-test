package com.amadeus.drools.rule.model;

import java.util.ArrayList;
import java.util.List;

public class Rule {

	private String name;
	private List<Attribute> attributes = new ArrayList<Attribute>();
	private Lhs lhs;
	private Rhs rhs;

	private List<Constraint> constraints = new ArrayList<Constraint>();
	private List<Consequence> consequences = new ArrayList<Consequence>();
	
	public Rule(){
		lhs = new Lhs();
		rhs = new Rhs();
	}

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
		if (attr != null)
			this.attributes.add(attr);
	}

	public void addConsequence(Consequence consequence) {
		this.consequences.add(consequence);
	}

	public Lhs getLhs() {
		return lhs;
	}

	public void setLhs(Lhs lhs) {
		this.lhs = lhs;
	}

	public Rhs getRhs() {
		return rhs;
	}

	public void setRhs(Rhs rhs) {
		this.rhs = rhs;
	}

	public String buildRule() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n");
		builder.append("rule \"");
		builder.append(this.name);
		builder.append("\"");
		builder.append("\n");
		for (Attribute attribute : attributes) {
			builder.append("  "+attribute.getName()+" "+attribute.getValue());
			builder.append("\n");
		}
		builder.append(" when");
		builder.append("\n");
		builder.append(this.lhs.buildLhs());
		builder.append("then");
		builder.append("\n");
		builder.append(this.rhs.buildRhs());
		builder.append("\n");
		builder.append(" end");
		return builder.toString();
	}
}
