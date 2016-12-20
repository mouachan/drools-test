package com.amadeus.drools.rule.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="rule")
@XmlAccessorType(XmlAccessType.FIELD)
public class Rule {
	
	@XmlElement(name="name")
	private String name;
	@XmlElement(name="attributes")
	private List<RuleAttribute> attributes = new ArrayList<RuleAttribute>();
	@XmlElement(name="lhs")
	private Lhs lhs;
	@XmlElement(name="rhs")
	private Rhs rhs;

	public Rule() {
		lhs = new Lhs();
		rhs = new Rhs();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the attributes
	 */
	public List<RuleAttribute> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes
	 *            the attributes to set
	 */
	public void setAttributes(List<RuleAttribute> attributes) {
		this.attributes = attributes;
	}

	/**
	 * @return the lhs
	 */
	public Lhs getLhs() {
		return lhs;
	}

	/**
	 * @param lhs
	 *            the lhs to set
	 */
	public void setLhs(Lhs lhs) {
		this.lhs = lhs;
	}

	/**
	 * @return the rhs
	 */
	public Rhs getRhs() {
		return rhs;
	}

	/**
	 * @param rhs
	 *            the rhs to set
	 */
	public void setRhs(Rhs rhs) {
		this.rhs = rhs;
	}

	public void addAttribute(RuleAttribute attr) {
		if (attr != null)
			this.attributes.add(attr);
	}

	public String buildRule() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n");
		builder.append("rule \"");
		builder.append(this.name);
		builder.append("\"");
		builder.append("\n");
		for (RuleAttribute attribute : attributes) {
			builder.append("  " + attribute.getName() + " " + attribute.getValue());
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
