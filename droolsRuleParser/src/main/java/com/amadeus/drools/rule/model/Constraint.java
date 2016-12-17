package com.amadeus.drools.rule.model;

public class Constraint {
	private String objectType;
	private String prefix;
	private String left;
	private String operand;
	private String right;
	private int index;
	
	
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public String getLeft() {
		return left;
	}
	public void setLeft(String left) {
		this.left = left;
	}
	public String getOperand() {
		return operand;
	}
	public void setOperand(String operand) {
		this.operand = operand;
	}
	public String getRight() {
		return right;
	}
	public void setRight(String right) {
		this.right = right;
	}
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
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
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Constraint [objectType=");
		builder.append(objectType);
		builder.append(", left=");
		builder.append(left);
		builder.append(", operand=");
		builder.append(operand);
		builder.append(", right=");
		builder.append(right);
		builder.append(", index=");
		builder.append(index);
		builder.append("]");
		return builder.toString();
	}

	

}
