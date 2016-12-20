package com.amadeus.drools.rule.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="expression")
@XmlAccessorType(XmlAccessType.FIELD)
public class Expression {
	@XmlElement(name="type")
	private String type;
	@XmlElement(name="objectType")
	private String objectType;
	@XmlElement(name="prefix")
	private String prefix;
	@XmlElement(name="bindingType")
	private String bindingType;
	@XmlElement(name="constraints")
	private List<Constraint> constraints = new ArrayList<Constraint>();
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
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
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}
	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Line [type=");
		builder.append(type);
		builder.append(", objectType=");
		builder.append(objectType);
		builder.append(", prefix=");
		builder.append(prefix);
		builder.append(", constraints=");
		builder.append(constraints);
		builder.append("]");
		return builder.toString();
	} 
}
