package com.amadeus.drools.rule.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="function")
@XmlAccessorType(XmlAccessType.FIELD)
public class Function {
	@XmlElement(name="name")
	private String name;
	@XmlElement(name="returnType")
	private String returnType;
	@XmlElement(name="parameters")
	private List<Parameter> parameters = new ArrayList<Parameter>();
	@XmlElement(name="body")
	private String body;

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
	 * @return the returnType
	 */
	public String getReturnType() {
		return returnType;
	}

	/**
	 * @param returnType
	 *            the returnType to set
	 */
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	/**
	 * @return the attributes
	 */
	public List<Parameter> getParameters() {
		return parameters;
	}

	/**
	 * @param attributes
	 *            the attributes to set
	 */
	public void setAttributes(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	public void addParameter(Parameter param) {
		this.parameters.add(param);
	}

	public String buildFunction() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n");
		builder.append("function ");
		builder.append(this.returnType);
		builder.append(" ");
		builder.append(this.name);
		builder.append("(");
		int index = 0;
		for (Parameter param : this.parameters) {
			index++;
			if (index < this.parameters.size())
				builder.append(param.getType() + " " + param.getName() + ",");
			else
				builder.append(param.getType() + " " + param.getName());
		}
		builder.append(")");
		builder.append("{");
		builder.append("\n");
		builder.append(this.body);
		builder.append("\n");
		builder.append("}");
		return builder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Function [name=");
		builder.append(name);
		builder.append(", parameters=");
		builder.append(parameters);
		builder.append(", body=");
		builder.append(body);
		builder.append("]");
		return builder.toString();
	}

}
