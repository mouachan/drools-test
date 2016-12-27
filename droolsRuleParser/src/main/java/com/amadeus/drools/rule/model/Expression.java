package com.amadeus.drools.rule.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlRootElement(name="expression")
@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT)
@JsonPropertyOrder({ "bindingField", "objectType", "constraints"})
public class Expression{
	
	@XmlElement(name="bindingType")
	private String bindingType;
	@XmlElement(name="objectType")
	private String objectType;
	@XmlElement(name="constraints")
	private List<Constraint> constraints = new ArrayList<Constraint>();


	/**
	 * @return the objectType
	 */
	public String getObjectType() {
		return objectType;
	}
	/**
	 * @param objectType the objectType to set
	 */
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	/**
	 * @return the bindingType
	 */
	public String getBindingType() {
		return bindingType;
	}
	/**
	 * @param bindingType the bindingType to set
	 */
	public void setBindingType(String bindingType) {
		this.bindingType = bindingType;
	}
	
	/**
	 * @return the constraints
	 */
	public List<Constraint> getConstraints() {
		return constraints;
	}
	/**
	 * @param constraints the constraints to set
	 */
	public void setConstraints(List<Constraint> constraints) {
		this.constraints = constraints;
	}
	
	public void addConstraint(Constraint constraint){
		this.constraints.add(constraint);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Expression [bindingType=");
		builder.append(bindingType);
		builder.append(", objectType=");
		builder.append(objectType);
		builder.append(", constraints=");
		builder.append(constraints);
		builder.append("]");
		return builder.toString();
	}
}
