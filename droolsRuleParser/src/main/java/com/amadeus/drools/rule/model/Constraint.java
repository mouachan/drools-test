package com.amadeus.drools.rule.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="constraint")
@XmlAccessorType(XmlAccessType.FIELD)
public class Constraint {
	@XmlElement(name="type")
	private String type;
	@XmlElement(name="objectType")
	private String objectType;
	@XmlElement(name="prefix")
	private String prefix;
	@XmlElement(name="left")
	private String left;
	@XmlElement(name="operand")
	private String operand;
	@XmlElement(name="right")
	private String right;
	@XmlElement(name="bindingField")
	private String bindingField;
	@XmlElement(name="variable")
	private String variable;
	@XmlElement(name="content")
	private String content;
	@XmlElement(name="index")
	private int index;

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
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
	 * @param objectType
	 *            the objectType to set
	 */
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

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

	/**
	 * @return the variable
	 */
	public String getVariable() {
		return variable;
	}

	/**
	 * @param variable
	 *            the variable to set
	 */
	public void setVariable(String variable) {
		this.variable = variable;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix
	 *            the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Constraint [objectType=");
		builder.append(objectType);
		builder.append(",Type=");
		builder.append(type);
		if (type != null && type.equals("eval"))
			builder.append(content);
		else {
			builder.append(",Prefix=");
			builder.append(prefix);
			builder.append(", left=");
			builder.append(left);
			builder.append(", operand=");
			builder.append(operand);
			builder.append(", right=");
			builder.append(right);
		}
		builder.append(", index=");
		builder.append(index);
		builder.append("]");
		return builder.toString();
	}

}
