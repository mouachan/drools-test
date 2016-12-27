package com.amadeus.drools.rule.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@XmlRootElement(name = "node")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT)
@JsonPropertyOrder({ "gate", "expression", "children"})
public class Node {

	@XmlEnum
	public enum Gate {
		OR, AND
	};
	@XmlElement(name = "gate")
	private Gate gate;
	
	@JsonInclude(Include.NON_NULL)
	@XmlElement(name = "children")
	private List<Node> children = new ArrayList<Node>();
	@JsonInclude(Include.NON_NULL)
	@XmlElement(name = "expression")
	private Expression expression;

	public List<Node> getChildren() {
		return children;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public Gate getGate() {
		return gate;
	}

	public void setGate(Gate gate) {
		this.gate = gate;
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	public void addChildren(Node children) {
		this.children.add(children);
	}

	@JsonIgnore
	public boolean expNotNull() {
		return (expression != null);
	}

	public int numExpression() {
		int i = 0;
		for (Node child : children) {
			if (child.expNotNull())
				i++;
		}
		return i;

	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Node [gate=");
		builder.append(gate);
		builder.append(", children=");
		builder.append(children);
		builder.append(", expression=");
		builder.append(expression);
		builder.append("]");
		return builder.toString();
	}



}
