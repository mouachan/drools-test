package com.amadeus.drools.rule.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
@XmlRootElement(name="constraint")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT)
@JsonPropertyOrder({ "bindingField", "left", "operand","right"})
public class Constraint {


	@XmlElement(name="bindingField")
	private String bindingField;
	@XmlElement(name="left")
	private String left;
	@XmlElement(name="operand")
	private String operand;
	@XmlElement(name="right")
	private String right;


	/**
	 * @return the left
	 */
	public String getLeft() {
		return left;
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(String left) {
		this.left = left;
	}

	/**
	 * @return the operand
	 */
	public String getOperand() {
		return operand;
	}

	/**
	 * @param operand
	 *            the operand to set
	 */
	public void setOperand(String operand) {
		this.operand = operand;
	}

	/**
	 * @return the right
	 */
	public String getRight() {
		return right;
	}

	/**
	 * @param right
	 *            the right to set
	 */
	public void setRight(String right) {
		this.right = right;
	}

	/**
	 * @return the bindingField
	 */
	public String getBindingField() {
		return bindingField;
	}

	/**
	 * @param bindingField
	 *            the bindingField to set
	 */
	public void setBindingField(String bindingField) {
		this.bindingField = bindingField;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Constraint [bindingField=");
		builder.append(bindingField);
		builder.append(", left=");
		builder.append(left);
		builder.append(", operand=");
		builder.append(operand);
		builder.append(", right=");
		builder.append(right);
		builder.append("]");
		return builder.toString();
	}

	
	


}
